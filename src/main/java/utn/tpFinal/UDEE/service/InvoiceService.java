package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import utn.tpFinal.UDEE.exceptions.ClientNotFoundException;
import utn.tpFinal.UDEE.exceptions.DatesBadRequestException;
import utn.tpFinal.UDEE.exceptions.ResidenceNotFoundException;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.model.Dto.ConsumptionDto;
import utn.tpFinal.UDEE.model.Dto.InvoiceResponseDto;
import utn.tpFinal.UDEE.model.Invoice;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.repository.ClientRepository;
import utn.tpFinal.UDEE.repository.InvoiceRepository;
import utn.tpFinal.UDEE.repository.ResidenceRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {

    InvoiceRepository invoiceRepository;
    ResidenceRepository residenceRepository;
    ClientRepository clientRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository,ResidenceRepository residenceRepository,ClientRepository clientRepository){
        this.invoiceRepository = invoiceRepository;
        this.residenceRepository = residenceRepository;
        this.clientRepository = clientRepository;
    }

    public Page<InvoiceResponseDto> getUnpaidInvoices(Integer residenceId, Integer page, Integer size, List<Sort.Order> orders) throws ResidenceNotFoundException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        if(!residenceRepository.existsById(residenceId)){
            throw new ResidenceNotFoundException(this.getClass().getSimpleName(),"getUnpaidInvoices");
        }

        Page<Invoice> invoices = invoiceRepository.findByPaidAndResidenceId(false,residenceId,pageable);


        Page<InvoiceResponseDto> invoiceDtos = Page.empty();
        if(!invoices.isEmpty()){
            invoiceDtos = invoices.map(i-> InvoiceResponseDto.from(i));
        }
        return invoiceDtos;
    }

    public Page<InvoiceResponseDto> getClientUnpaidInvoices(Integer idClient, Integer page, Integer size, List<Sort.Order> orders) throws ClientNotFoundException {
        Client c = clientRepository.findById(idClient).orElseThrow(()->new ClientNotFoundException(this.getClass().getSimpleName(),"getClientUnpaidInvoices"));
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        //Elegir entre loop o query con array de Ids
        List<Integer> residenceIds = new ArrayList<Integer>();

        for(Residence residence : c.getResidences()){
            residenceIds.add(residence.getId());
        }

        Page<Invoice> invoices = invoiceRepository.findByPaidAndResidenceIdIn(false,residenceIds,pageable);

        Page<InvoiceResponseDto> invoiceDtos = Page.empty();
        if(!invoices.isEmpty()){
            invoiceDtos = invoices.map(i-> InvoiceResponseDto.from(i));
        }
        return invoiceDtos;
    }
    public ConsumptionDto getConsumtionBetweenDates(Integer dniClient, Date from, Date to) throws ClientNotFoundException, DatesBadRequestException {
        if(from.after(to)){
            throw new DatesBadRequestException(this.getClass().getSimpleName(),"getConsumtionBetweenDates");
        }
        if(!clientRepository.existsById(dniClient)){
            throw new ClientNotFoundException(this.getClass().getSimpleName(),"getConsumptionBetweenDates");
        }

        List<Invoice> invoices = invoiceRepository.findByClientDniAndEmissionDateBetween(dniClient,from,to);

        ConsumptionDto consumptionDto = ConsumptionDto.builder().dni(dniClient).totalConsumption(0f).build();
        if(!invoices.isEmpty()){
            consumptionDto.setInvoices(InvoiceResponseDto.from(invoices));
            for(Invoice i: invoices){
                consumptionDto.setTotalConsumption((consumptionDto.getTotalConsumption()+ i.getTotalConsumption()));
            }
        }

        return consumptionDto;
    }
}

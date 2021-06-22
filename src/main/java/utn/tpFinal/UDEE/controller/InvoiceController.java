package utn.tpFinal.UDEE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utn.tpFinal.UDEE.model.Invoice;
import utn.tpFinal.UDEE.service.InvoiceService;

import java.util.List;

@RestController
@RequestMapping("/backoffice/invoices")
public class InvoiceController {

    InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PostMapping
    public void addInvoice(@RequestBody Invoice invoice) {
        invoiceService.add(invoice);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping
    public List<Invoice> getAll(){
        return invoiceService.getAll();
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Integer id) {
        return invoiceService.getByID(id);
    }
}

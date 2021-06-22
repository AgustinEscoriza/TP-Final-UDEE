package utn.tpFinal.UDEE.util;

import org.springframework.data.domain.Sort;
import utn.tpFinal.UDEE.model.Dto.MeasureRequestDto;
import utn.tpFinal.UDEE.model.Dto.MeasureResponseDto;
import utn.tpFinal.UDEE.model.Measurement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestConstants {

    public static List<Sort.Order> getOrders(String field1, String field2){
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, field1));
        orders.add(new Sort.Order(Sort.Direction.ASC, field2));

        return orders;
    }
    public static Measurement getMeasure(Integer id) throws ParseException {
        return Measurement.builder()
                .id(id)
                .kwH(100F)
                .date(getFecha(1))
                .billed(false)
                .build();
    }

    public static MeasureRequestDto getMeasureRequestDto() throws ParseException {
        return MeasureRequestDto.builder()
                .value(100F)
                .date("2021-06-02 12:12:12")
                .password("1234")
                .serialNumber("001")
                .build();
    }
    public static Date getFecha(Integer i) throws ParseException {
        Date date1 =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-02");
        Date date2 =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-02");
        Date rta=new Date();
        switch (i){
            case 1:
                rta= date1;
                break;
            case 2:
                rta= date2;
                break;
        }
        return rta;
    }


}

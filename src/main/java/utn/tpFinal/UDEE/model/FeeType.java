package utn.tpFinal.UDEE.model;


import java.util.stream.Stream;

public enum FeeType {
    RESIDENTIAL(1,"RESIDENTIAL",60),
    COMERTIAL(2,"COMERTIAL",75),
    INDUSTRIAL(3,"INDUSTRIAL",40),
    OTHER(4,"OTHER",65);

    private Integer id;
    private String detail;
    private Integer kwPricePerHour;

    FeeType(Integer id, String detail, Integer pricePerKwh) {
        this.id = id;
        this.detail = detail;
        this.kwPricePerHour = pricePerKwh;
    }

    public Integer getId() { return id; }

    public String getDetail() {
        return detail;
    }

    public Integer getKwPricePerHour() {
        return kwPricePerHour;
    }

    public static FeeType of(int feeValue) {
        return Stream.of(FeeType.values())
                .filter(p -> p.getId() == feeValue)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

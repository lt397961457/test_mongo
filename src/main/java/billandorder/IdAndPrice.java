package billandorder;

import lombok.Data;

import java.util.Date;

@Data
public class IdAndPrice {
    private Integer price;
    private Integer sum;
    private String appId ;
    private String appName;
    private String productName;
    private Integer productId;
}

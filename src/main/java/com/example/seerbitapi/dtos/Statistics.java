
package com.example.seerbitapi.dtos;

import java.math.BigDecimal;

/**
 *
 * @author HP
 */
public record Statistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, long count ){
    
}

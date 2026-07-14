package com.buchu.marketpulse.alert;

import java.math.BigDecimal;

public record CreateAlertRequest(String symbol, AlertCondition condition, BigDecimal targetPrice) {
}

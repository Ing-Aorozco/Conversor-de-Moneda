import java.time.LocalDateTime;

public class Conversion {
    private double amount;
    private String baseCurrency;
    private String targetCurrency;
    private double conversionResult;
    private LocalDateTime timestamp;

    public Conversion(
            double amount,
            String baseCurrency,
            String targetCurrency,
            double conversionResult,
            LocalDateTime timestamp
    ) {
        this.amount = amount;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.conversionResult = conversionResult;
        this.timestamp = timestamp;
    }

    // Getters
    public double getAmount() {
        return amount;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public double getConversionResult() {
        return conversionResult;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public void setConversionResult(double conversionResult) {
        this.conversionResult = conversionResult;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("Conversión: %.2f %s --> %.2f %s | Fecha y hora: %s",
                amount, baseCurrency, conversionResult, targetCurrency, timestamp);
    }
}

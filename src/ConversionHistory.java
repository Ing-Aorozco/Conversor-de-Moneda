import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ConversionHistory {
    private LinkedList<Conversion> history;

    public ConversionHistory() {
        this.history = new LinkedList<>();
    }

    public void addRecord(double amount, String baseCurrency, String targetCurrency, double conversionResult) {
        if (history.size() == 30) {
            history.removeFirst();
        }
        Conversion record = new Conversion(amount, baseCurrency, targetCurrency, conversionResult, LocalDateTime.now());
        history.add(record);
    }

    public List<Conversion> getHistory() {
        return history;
    }

    public void printHistory() {
        if (history.isEmpty()) {
            System.out.println("Historial está Vacio");
        } else {
            System.out.println("Historial de Conversiones:");
            for (int i = 0; i < history.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, history.get(i));
            }
        }
    }

    public void saveHistoryToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            if (history.isEmpty()) {
                writer.write("Historial está Vacio");
            } else {
                writer.write("Historial de Conversiones:\n");
                for (int i = 0; i < history.size(); i++) {
                    writer.write(String.format("%d. %s%n", i + 1, history.get(i)));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al escribir el historial en el archivo: " + e.getMessage());
        }
    }
}

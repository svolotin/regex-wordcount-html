/*
 * 2018 Sami.
 */
package fi.sami.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author sami
 */
public class WordCount {

    private final double proportion;
    private final int foundwords;
    private final int total;
    private final String status;

    public WordCount(int found, int total, String status) {
        this.foundwords = found;
        this.total = total;

        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(custom);
        this.proportion = total > 0 ? Double.valueOf(df.format(found / (double) total)) : 0;
        this.status = status;
    }

    public double getProportion() {
        return this.proportion;
    }

    public double getFoundWords() {
        return this.foundwords;
    }

    public double getTotalWords() {
        return this.total;
    }

    public String getStatus() {
        return this.status;
    }

}

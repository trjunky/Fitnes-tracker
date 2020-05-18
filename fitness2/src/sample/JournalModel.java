package sample;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class JournalModel {

    Journal journalData;

    @XmlElement(name="journal")
    public Journal getJournalData() {
        return journalData;
    }

    public void setJournalData(Journal journalData) {
        this.journalData = journalData;
    }

}

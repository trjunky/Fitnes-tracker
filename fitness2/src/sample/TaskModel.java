package sample;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.io.*;


public class TaskModel implements java.io.Serializable

{
    private static final long serialVersionUID = 1L;
    public int kkal;
    // Конструктор по умолчанию
    public TaskModel(int kkals)
    {
        this.kkal=kkals;
    }
}


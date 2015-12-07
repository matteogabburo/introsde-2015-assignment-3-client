package client;

import introsde.document.ws.Measure;
import introsde.document.ws.People;
import introsde.document.ws.PeopleService;
import introsde.document.ws.Person;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by matteo on 06/12/15.
 */
public class Assignment3Client
{

    static String LOGNAME = "client-server.log";
    String log = "";

    public static void main(String[] args) throws Exception {

        PeopleService service = new PeopleService();
        People people = service.getPeopleImplementationPort();


        /*URL url = new URL("http://127.0.1.1:6902");
        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ws.document.introsde/", "PeopleService");
        Service service = Service.create(url, qname);

        People people = service.getPort(People.class);
*/
        Assignment3Client a = new Assignment3Client();

        //READ PERSON LIST

        a.log += "\n\n#1 READ PERSON LIST ==============\n";
        List<Person> pList = people.readPersonList();
        for(int i = 0; i < pList.size(); i++)
            a.printPerson(pList.get(i));

        a.log += "\n\n#2 READ PERSON ==============\n";
        Person p1 = people.readPerson((long) 151);
        a.printPerson(p1);

        a.log += "\n\n#3 UPDATE PERSON ==============\n";
        Person p3 = new Person();
        p3.setLastname(p1.getLastname());
        p3.setId(p1.getId());

        if(p1.getFirstname().equals("Ambrogio"))
            p3.setFirstname("Mario");
        else
            p3.setFirstname("Ambrogio");

        p1 = people.updatePerson(p3);
        a.printPerson(p1);

        a.log += "\n\n#4 CREATE PERSON ==============\n";
        Person p2 = a.makePerson("Artemisio", "Rossi");
        p2 =people.createPerson(p2);
        a.printPerson(p2);

        a.log += "\n\n#5 DELETE PERSON ==============\n";
        people.deletePerson(p2.getId());


        //READ PERSON HISTORY
        a.log += "\n\n#6 READ PERSON HISTORY ==============\n";
        List<Measure> mList = people.readPersonHistory(p1.getId(), "height");
        System.out.println(p1.getId() +"  "+ mList.size());
        Measure m;
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            a.printMeasure(m);
        }

        //READ MEASURE TYPES
        a.log += "\n\n#7 READ MEASURE TYPES ==============\n";
        List<String> types = people.readMeasureTypes();
        System.out.println("Types");
        for(int i = 0; i < types.size(); i++)
        {
            a.log += types.get(i);
        }

        //READ PERSON MEASURES
        a.log += "\n\n#8 READ PERSON MEASURE ==============\n";
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            a.printMeasure(m);
        }


        //SAVE PERSON MEASURE
        a.log += "\n\n#9 SAVE PERSON MEASURE ==============\n";
        Measure m1 = a.makeMeasure(1, "height", "123", "integer", p1);
        people.savePersonMeasure(p1.getId(), m1);
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            a.printMeasure(m);
        }

        //UPDATE PERSON MEASURE
        a.log += "\n\n#10 UPDATE PERSON MEASURE ==============\n";
        m1.setMeasureValue("678");
        m1 = people.updatePersonMeasure(p1.getId(), m1);
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            a.printMeasure(m);
        }

        a.makeLogs(a.log);
    }

    public Measure makeMeasure(long mid, String measureType, String value, String valueType, Person p) throws DatatypeConfigurationException
    {
        Measure m = new Measure();
        m.setMid(mid);
        m.setMeasureType(measureType);
        m.setMeasureValue(value);
        m.setMeasureValueType(valueType);
        m.setPerson(p);

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(c.getTime());
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        m.setDateRegistered(date);

        return m;
    }

    public Person makePerson(String firstname, String lastname)
    {
        Person p = new Person();

        p.setFirstname(firstname);
        p.setLastname(lastname);

        return p;
    }

    public void printPerson(Person p)
    {
        this.log += "PERSON =========================================";
        this.log +="\n";
        this.log +="id : \t"+p.getId();
        this.log +="firstName : \t"+p.getFirstname()+"\n";
        this.log +="lastName : \t"+p.getLastname()+"\n";

        Measure m;

        this.log +="HealthHistory"+"\n";
        List<Measure> pList = p.getHealthHistory();
        for(int i = 0; i < pList.size(); i++)
        {
            m = pList.get(i);
            this.printMeasure(m);
        }
        this.log +="CurrentHealth"+"\n";
        pList = p.getCurrentHealth();
        for(int i = 0; i < pList.size(); i++)
        {
            m = pList.get(i);
            this.printMeasure(m);
        }
        this.log +="\n";
        this.log +="================================================\n";
    }

    public void printMeasure(Measure m)
    {
        this.log +="\tmid :\t"+m.getMid()+"\n";
        this.log +="\tdateRegistered :\t"+m.getDateRegistered()+"\n";
        this.log +="\tmeasureType :\t"+m.getMeasureType()+"\n";
        this.log +="\tmeasureValue :\t"+m.getMeasureValue()+"\n";
    }

    private void makeLogs(String xmlResp) throws IOException {
        FileWriter logxml;
        logxml = new FileWriter(this.LOGNAME);

        BufferedWriter b;

        System.out.println("Writing log...");
        b=new BufferedWriter (logxml);
        b.write(xmlResp);
        b.flush();
    }
}
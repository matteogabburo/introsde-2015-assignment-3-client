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

        /*PeopleService service = new PeopleService();
        People people = service.getPeopleImplementationPort();*/

        String serverUrl = "https://hidden-earth-30101.herokuapp.com/ws/people?wsdl";
        //String serverUrl = "http://127.0.1.1:6902/ws/people?wsdl";


        URL url = new URL(serverUrl);
        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ws.document.introsde/", "PeopleService");
        Service service = Service.create(url, qname);

        People people = service.getPort(People.class);

        Assignment3Client a = new Assignment3Client();

        String tmp = "";

        //READ PERSON LIST

        tmp += serverUrl+"\n\n";

        tmp += "\n\n#1 READ PERSON LIST ==============\n";
        List<Person> pList = people.readPersonList();
        for(int i = 0; i < pList.size(); i++)
            tmp += a.printPerson(pList.get(i));
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        tmp += "\n\n#2 READ PERSON ==============\n";
        Person p1 = people.readPerson((long) 1);
        tmp +=a.printPerson(p1);
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        tmp += "\n\n#3 UPDATE PERSON ==============\n";
        Person p3 = new Person();
        p3.setLastname(p1.getLastname());
        p3.setId(p1.getId());

        if(p1.getFirstname().equals("Ambrogio"))
            p3.setFirstname("Mario");
        else
            p3.setFirstname("Ambrogio");

        p1 = people.updatePerson(p3);
        tmp += a.printPerson(p1);
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        tmp += "\n\n#4 CREATE PERSON ==============\n";
        Person p2 = a.makePerson("Artemisio", "Rossi");
        p2 =people.createPerson(p2);
        tmp += a.printPerson(p2);
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        tmp += "\n\n#5 DELETE PERSON ==============\n";
        people.deletePerson(p2.getId());
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        tmp += "\n\nDelete..\nCHECK IF CREATED PERSON IS REALLY DELETED\n";
        pList = people.readPersonList();
        for(int i = 0; i < pList.size(); i++)
            tmp += a.printPerson(pList.get(i));
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        //READ PERSON HISTORY
        tmp += "\n\n#6 READ PERSON HISTORY ==============\n";
        List<Measure> mList = people.readPersonHistory(p1.getId(), "height");
        Measure m;
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            tmp += a.printMeasure(m);
        }
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        //READ MEASURE TYPES
        tmp += "\n\n#7 READ MEASURE TYPES ==============\n";
        List<String> types = people.readMeasureTypes();
        System.out.println("Types");
        for(int i = 0; i < types.size(); i++)
        {
            tmp += types.get(i)+"\n";
        }
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        //READ PERSON MEASURES
        tmp += "\n\n#8 READ PERSON MEASURE ==============\n";
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            tmp += a.printMeasure(m);
        }
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";


        //SAVE PERSON MEASURE
        tmp += "\n\n#9 SAVE PERSON MEASURE ==============\n";
        Measure m1 = a.makeMeasure(1, "height", "123", "integer", p1);
        m1 = people.savePersonMeasure(p1.getId(), m1);
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            tmp += a.printMeasure(m);
        }
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

        //UPDATE PERSON MEASURE
        tmp += "\n\n#10 UPDATE PERSON MEASURE ==============\n";
        m1.setMeasureValue("678");
        System.out.println(m1.getIdMeasure());
        m1 = people.updatePersonMeasure(p1.getId(), m1);
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            tmp += a.printMeasure(m);
        }
        a.log += tmp;
        System.out.println(tmp);
        tmp = "";

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

    public String printPerson(Person p)
    {
        String res = "";

        res += "PERSON =========================================";
        res +="\n";
        res +="id : \t"+p.getId()+"\n";
        res +="firstName : \t"+p.getFirstname()+"\n";
        res +="lastName : \t"+p.getLastname()+"\n";

        Measure m;

        res +="HealthHistory"+"\n";
        List<Measure> pList = p.getHealthHistory();
        for(int i = 0; i < pList.size(); i++)
        {
            m = pList.get(i);
            res += this.printMeasure(m);
        }
        res +="CurrentHealth"+"\n";
        pList = p.getCurrentHealth();
        for(int i = 0; i < pList.size(); i++)
        {
            m = pList.get(i);
            res += this.printMeasure(m);
        }
        res +="\n";
        res +="================================================\n";

        return res;
    }

    public String printMeasure(Measure m)
    {
        String res = "";

        res +="\tmid :\t"+m.getMid()+"\n";
        res +="\tdateRegistered :\t"+m.getDateRegistered()+"\n";
        res +="\tmeasureType :\t"+m.getMeasureType()+"\n";
        res +="\tmeasureValue :\t"+m.getMeasureValue()+"\n";

        return res;
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

package client;

import introsde.document.ws.Measure;
import introsde.document.ws.People;
import introsde.document.ws.PeopleService;
import introsde.document.ws.Person;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by matteo on 06/12/15.
 */
public class Assignment3Client
{
    public static void main(String[] args) throws Exception {

        PeopleService service = new PeopleService();
        People people = service.getPeopleImplementationPort();

        Assignment3Client a = new Assignment3Client();

        //READ PERSON LIST
        List<Person> pList = people.readPersonList();
        for(int i = 0; i < pList.size(); i++)
            a.printPerson(pList.get(i));

        //READ PERSON
        Person p1 = people.readPerson((long) 151);
        a.printPerson(p1);

        //UPDATE PERSON
        Person p3 = new Person();
        p3.setLastname(p1.getLastname());
        p3.setId(p1.getId());

        if(p1.getFirstname().equals("Ambrogio"))
            p3.setFirstname("Mario");
        else
            p3.setFirstname("Ambrogio");

        p1 = people.updatePerson(p3);
        a.printPerson(p1);

        //CREATE PERSON
        Person p2 = a.makePerson("Artemisio", "Rossi");
        p2 =people.createPerson(p2);
        a.printPerson(p2);

        //DELETE PERSON
        people.deletePerson(p2.getId());


        //READ PERSON HISTORY
        List<Measure> mList = people.readPersonHistory(p1.getId(), "height");
        System.out.println("HealthHistory");
        Measure m;
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            a.printMeasure(m);
        }

        //READ MEASURE TYPES
        List<String> types = people.readMeasureTypes();
        System.out.println("Types");
        for(int i = 0; i < types.size(); i++)
        {
            System.out.println(types.get(i));
        }

        //READ PERSON MEASURES
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            a.printMeasure(m);
        }


        //SAVE PERSON MEASURE
        Measure m1 = a.makeMeasure(1, "height", "123", "integer", p1);
        people.savePersonMeasure(p1.getId(), m1);
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            a.printMeasure(m);
        }

        //UPDATE PERSON MEASURE
        m1.setMeasureValue("678");
        m1 = people.updatePersonMeasure(p1.getId(), m1);
        mList = people.readPersonMeasures(p1.getId(), "height" ,(long)1);
        for(int i = 0; i < mList.size(); i++)
        {
            m = mList.get(i);
            a.printMeasure(m);
        }
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
        System.out.println("PERSON =========================================");
        System.out.println("");
        System.out.println("id : \t"+p.getId());
        System.out.println("firstName : \t"+p.getFirstname());
        System.out.println("lastName : \t"+p.getLastname());

        Measure m;

        System.out.println("HealthHistory");
        List<Measure> pList = p.getHealthHistory();
        for(int i = 0; i < pList.size(); i++)
        {
            m = pList.get(i);
            this.printMeasure(m);
        }
        System.out.println("CurrentHealth");
        pList = p.getCurrentHealth();
        for(int i = 0; i < pList.size(); i++)
        {
            m = pList.get(i);
            this.printMeasure(m);
        }
        System.out.println("");
        System.out.println("================================================");
    }

    public void printMeasure(Measure m)
    {
        System.out.println("\tmid :\t"+m.getMid());
        System.out.println("\tdateRegistered :\t"+m.getDateRegistered());
        System.out.println("\tmeasureType :\t"+m.getMeasureType());
        System.out.println("\tmeasureValue :\t"+m.getMeasureValue());
    }
}
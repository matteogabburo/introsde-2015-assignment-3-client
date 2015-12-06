package client;

import introsde.document.ws.People;
import introsde.document.ws.PeopleService;
import introsde.document.ws.Person;

import java.util.List;

/**
 * Created by matteo on 06/12/15.
 */
public class Assignment3Client
{
    public static void main(String[] args) throws Exception {
        PeopleService service = new PeopleService();

        People people = service.getPeopleImplementationPort();


        //Create Person
       /* Person p1 = new Person();
        p1.setFirstname("Primo");
        p1.setLastname("Rossi");

        people.createPerson(p1);
          */
        //Read Person
        Person p = people.readPerson((long) 1);

        System.out.println(p.getFirstname());
        System.out.println(p.getLastname());

        /*List<Person> pList = people.readPersonList();
        System.out.println("Result ==> "+p);
        System.out.println("Result ==> "+pList);
        System.out.println("First Person in the list ==> "+pList.get(0).getFirstname());*/
    }
}

/*
package client;

        import java.util.List;

        import introsde.document.ws.PeopleService;
        import introsde.document.ws.People;
        import introsde.document.ws.Person;

public class PeopleClient{
    public static void main(String[] args) throws Exception {
        PeopleService service = new PeopleService();
        People people = service.getPeopleImplPort();
        Person p = people.readPerson(1);
        List<Person> pList = people.getPeopleList();
        System.out.println("Result ==> "+p);
        System.out.println("Result ==> "+pList);
        System.out.println("First Person in the list ==> "+pList.get(0).getName());
    }
}*/
import java.util.*;
public class ObligSBinTre<T> implements Beholder<T>
{
    private static final class Node<T> // en indre nodeklasse
    {
        private T verdi; // nodens verdi
        private Node<T> venstre, høyre; // venstre og høyre barn
        private Node<T> forelder; // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
        {
            this.verdi = verdi;
            venstre = v; høyre = h;
            this.forelder = forelder;
        }
        private Node(T verdi, Node<T> forelder) // konstruktør
        {
            this(verdi, null, null, forelder);
        }
        @Override
        public String toString()
        {
            return "" + verdi;
        }

    } // class Node




    private Node<T> rot; // peker til rotnoden
    private int antall; // antall noder
    private int endringer; // antall endringer
    private final Comparator<? super T> comp; // komparator
    public ObligSBinTre(Comparator<? super T> c) // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi)
    {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier");

        Node<T> p = rot;
        Node<T> q = null;
        int cmp = 0;        // Hjelpevariabel

        while (p != null)     // Fortsetter til p  er ute av treet
        {
            q = p;              // q er foreldre til p
            cmp = comp.compare(verdi, p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;      // flytter p
        }

                    // p er nå null (ute av treet), q er den siste vi gikk igjennom

         p = new Node <>(verdi, q);         // q er forelder til p

        if (q == null)
        {
            rot = p;            // p blir rotnode

        } else if (cmp < 0)
        {
            q.venstre = p;      // venstre barn til q

        } else
            {
                q.høyre = p;        // høyre barn til q
            }

        antall++;           // Øker antall
        endringer ++;       // Øker endringer
        return true;

    }

    @Override
    public boolean inneholder(T verdi)
    {
        if (verdi == null) return false;
        Node<T> p = rot;
        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi)
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi)
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public int antall()
    {
        return antall;
    }

    public int antall(T verdi)
    {
        if (verdi == null)   // Hvis verdi ikke finnes i treet, returner null
        {
            return 0;
        }

        Node<T> p = rot;        // Oppretter en rot node
        int antallVerdi = 0;     // Hjelpevariabel

        while (p != null)
        {
            int cmp = comp.compare(verdi,p.verdi); //(verdi > p.verdi = 1) (verdi < p.verdi = -1) (verdi == p.verdi = 0)

            if (cmp < 0)                // Sjekker om cmp er mindre enn null
            {
                p = p.venstre;          // Sett rot  til rot sin venstre

            } else {
                if (cmp == 0)           // Hvis verdi er lik cmp
                {
                    antallVerdi++;      // Øk antall
                }
                p = p.høyre;            // Hvis cmp større enn verdi, flytt til høyre
            }
         } return antallVerdi;      // Returner verdi
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public void nullstill()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nesteInorden(Node<T> p)
    {

        if (p.høyre != null)
        {
            p = p.høyre;
            
            while (p.venstre != null)
            {
                p = p.venstre;
            }
        } else
            {
                while (p.forelder != null && p.forelder.høyre == p)
                {
                  p = p.forelder;
                }

           p = p.forelder;

            }
                return p;
    }

    @Override
    public String toString()
    {
        if (tom())
        {
            return "[]";
        }
        Node<T> p = rot;        // går til den første i inorden
        while (p.venstre != null)
        {
            p = p.venstre;
        }
        StringJoiner s = new StringJoiner(", ", "[", "]");


        while (p != null)
        {
            s.add(p.verdi.toString());
            p = nesteInorden(p);
        }

        return s.toString();
    }

    public String omvendtString()
    {
        if (tom())
        {
            return "[]";
        }

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Stack<Node<T>> stack = new Stack<>();  // Oppretter en stack
        Node<T> p = rot;            // Begynner i rot

        while (p.høyre != null)     // Går lengst mot høyre
        {
            stack.push(p);      // Legger til p.verdi på stack
            p = p.høyre;
        }

        s.add(p.verdi.toString());      // Legger p.verdi inn i tostring metoden

        while (true)
        {
            if (p.venstre != null)      // Sjekker om venstre verdi er gyldig
            {
                p = p.venstre;          // Flytter p

            while (p.høyre != null)     // Så lenge p.høyre ikke er null
            {
                stack.push(p);          // Legg til på stack
                p = p.høyre;            // Flytt p
                }

            } else if(!stack.empty())       // Sjekker om stack er tom
            {
                p = stack.pop();            // Hent siste verdi i stack

            } else break;

            s.add(p.verdi.toString());   // Legger til i stringjoiner
        }

        return s.toString();        // Returnerer toString av stringjoiner

    }

    public String høyreGren()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }




    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;


        private BladnodeIterator() // konstruktør
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }


        @Override
        public boolean hasNext()
        {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

    } // BladnodeIterator




                        /** MAIN METODE SOM MÅ FJERNES ETTER ENDT JOBBING */


    public static void main(String[] args) {
       /* // Tester at klassene er satt opp riktig
        ObligSBinTre<String> tre = new ObligSBinTre<> (Comparator.naturalOrder());
        System.out.println(tre.antall());

        // Test av oppgave 1
        Integer[] a = {4,7,2,9,5,10,8,1,3,6};
        ObligSBinTre <Integer> tre1 = new ObligSBinTre<>(Comparator.naturalOrder());

        for(int verdi : a)
        {
            tre1.leggInn(verdi);
        }
        System.out.println(tre.antall());  // Utskrift: 10


        // Test av oppgave 2

        Integer[] a = {4,7,2,9,4,10,8,7,4,6};
        ObligSBinTre <Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());

        for(int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.antall());
        System.out.println(tre.antall(5));
        System.out.println(tre.antall(4));
        System.out.println(tre.antall(7));
        System.out.println(tre.antall(10));


        // Test oppgave 3
        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre); */


       // Test oppgave 4

        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi : a) tre.leggInn(verdi);

        System.out.println(tre.omvendtString());  // [10, 9, 8, 7, 7, 6, 4, 4,4, 2, 1]

    }






} // ObligSBinTre

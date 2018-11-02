import java.util.*;
import java.util.function.Consumer;

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
        if (verdi == null || !inneholder(verdi))          // Sjekker etter nullverdier i treet
        {
            return false;
        }

        Node<T> p = rot;                // Oppretter rot node og forelder node q
        Node<T> q = null;


        while (p != null)           // leter etter verdi
        {
            int cmp = comp.compare(verdi, p.verdi);  // Sammenligner insatt verdi og p.verdi

            if (cmp < 0)
            {
                q = p;
                p = p.venstre;          // Går mot venstre
            } else if (cmp > 0)
            {
                q = p;
                p = p.høyre;           // Går mot høyre
            } else if (cmp == 0){
                break;              // Verdi er lik p.verdi
            }
        }

            if (p == null)
            {
                return false;       // Finner ingen verdi
            }

            if (p.venstre == null || p.høyre == null)   // Sjekker om det er barn
            {
                Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn

                if (b != null) {
                    b.forelder = q;
                }

                if (p == rot)
                {
                    rot = b;

                } else if (p == q.venstre)
                {
                    q.venstre = b;

                } else
                    {
                    q.høyre = b;
                }
            }
            else
                {
                    Node<T> s = p;
                    Node<T> r = p.høyre;   // finner neste i inorden

                    while (r.venstre != null)
                    {
                        s = r;    // s er forelder til r
                        r = r.venstre;
                    }
                     p.verdi = r.verdi;   // kopierer verdien i r til p

                    if (r.høyre != null)                // Koden er endret her for å sette forelder
                    {
                        r.høyre.forelder = s;
                    }
                    if (s != p)
                    {
                        s.venstre = r.høyre;
                    }
                    else {
                        s.høyre = r.høyre;
                    }
                }

            antall--;   // det er nå én node mindre i treet
            endringer ++;
            return true;

    }

    public int fjernAlle(T verdi)
    {

        int antForekomster = 0;

        while (fjern(verdi))
        {
            antForekomster++;
        }

        return antForekomster;

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

        if (!tom())
        {
            nullstill(rot);
        }
        rot = null;
        antall = 0;
        endringer++;

    }

    private static <T> void nullstill (Node <T> p)
    {
        if (p.venstre != null)
        {
            nullstill(p.venstre);
            p.venstre =null;
        }

        if (p.høyre != null)
        {
            nullstill(p.høyre);
            p.høyre = null;
        }

        p.verdi = null;
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
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        if (!tom())
        {
            Node <T> p = rot;

            while (true)
            {
                sj.add(p.verdi.toString());

              if (p.høyre != null)
                {
                    p = p.høyre;

                } else if (p.venstre != null)
                {
                    p = p.venstre;
                } else
                    {
                        break;
                    }
            }
        } return sj.toString();
    }



    public String lengstGren(){

        if (tom())
        {
            return "[]";
        }

        Deque<Node<T>> kø = new LinkedList<>();
        kø.add(rot);
        Node<T> p = null;

        while (!kø.isEmpty())
        {
            p = kø.remove();

            if(p.høyre != null)
            {
                kø.add(p.høyre);
            }

            if(p.venstre != null)
            {
                kø.add(p.venstre);
            }
        }

        return print(p);

    }

    public String print(Node<T> p ){
        Stack<T> stakk = new Stack<>();
        while (p != null){
            stakk.add(p.verdi);
            p = p.forelder;
        }
        StringJoiner joiner = new StringJoiner(", ","[","]");
        while (!stakk.isEmpty()){
            joiner.add(String.valueOf(stakk.pop()));
        }
        return joiner.toString();
    }


    public String[] grener()
    {

        List<String> list = new ArrayList<>();
        ArrayDeque<T> branch = new ArrayDeque<>();


        if (!tom())
        {
            grener(rot, list, branch);
        }

        return list.toArray(new String[0]);
    }


    private static <T> void grener(Node<T> p, List<String> liste, ArrayDeque<T> gren)
    {
        gren.addLast(p.verdi);

        if (p.venstre != null)
        {
            grener(p.venstre, liste, gren);

        }

        if (p.høyre != null)
        {
            grener(p.høyre, liste, gren);
        }

        if (p.høyre == null && p.venstre == null)
        {
            liste.add(gren.toString());
        }

        gren.removeLast();
    }


    public String bladnodeverdier()
    {
        if (tom())
        {
            return "[]";
        }

        StringJoiner stringJ = new StringJoiner(", ", "[", "]");

        bladnodeverdier(rot, stringJ);

        return stringJ.toString();
    }



    private static <T> void bladnodeverdier(Node<T> p, StringJoiner s)
    {
        if (p.venstre == null && p.høyre == null)
        {
            s.add(p.verdi.toString());
        }

        if (p.venstre != null)
        {
            bladnodeverdier(p.venstre, s);
        }

        if (p.høyre != null)
        {
            bladnodeverdier(p.høyre, s);
        }
    }

    private static <T> Node<T> firstLeafnode(Node<T> p)
    {
        while (true)
        {
            if (p.venstre != null)
            {
                p = p.venstre;
            } else if (p.høyre != null)
            {
                p = p.høyre;
            }

            else return p;
        }
    }



    public String postString()
    {
        if (tom())
        {
            return "[]";
        }

        Node<T> p = firstLeafnode(rot);
        StringJoiner stringJ = new StringJoiner(", ", "[", "]");


        while (true)
        {

            stringJ.add(p.verdi.toString());

            if (p.forelder == null)
            {
                break;
            }

            Node<T> f = p.forelder;

            if (p == f.høyre || f.høyre == null)
            {
                p = f;
            } else
            {
                p = firstLeafnode(f.høyre);
            }
        }

        return stringJ.toString();
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
            if (tom()) {
                return;
            }
            p = firstLeafnode(rot);
            q = null;
            removeOK = false;
            iteratorendringer = endringer;

        }


        @Override
        public boolean hasNext()
        {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException("ikke flere bladnodeverdier.");

            if (endringer != iteratorendringer) throw new ConcurrentModificationException("Treet har blit endret.");

            removeOK = true;
            q = p;
            p = nextLeafnode(p);

            return q.verdi;

        }

        private  <T> Node<T> nextLeafnode (Node<T> p){
            Node<T> fa = p.forelder;
            while (fa !=null && (p== fa.høyre || fa.høyre == null)){
                p= fa;
                fa = fa.forelder;
            }
            return fa == null ? null : firstLeafnode(fa.høyre);
        }

        @Override
        public void remove()
        {
            if (!removeOK){
                throw new IllegalStateException("Ulovlig kall pa metoden remove");
            }

            if (endringer != iteratorendringer){
                throw new ConcurrentModificationException("Treet har blitt endret");
            }


            removeOK = false;

            Node<T> fa = q.forelder;

            if (fa == null)
            {
                rot = null;
            }
            else if (q == fa.venstre) {
                fa.venstre = null;
            }
            else {
                fa.høyre = null;
            }

            antall--;
            endringer++;
            iteratorendringer++;
        }

    } // BladnodeIterator

} // ObligSBinTre

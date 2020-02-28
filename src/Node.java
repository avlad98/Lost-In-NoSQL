import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;

public class Node {
    private LinkedList<Instance> instances;
    private int nodeIndex;
    private int maxCapacity;

    /**
     * Constructorul pentru nod
     * Fiecare nod are cate o lista pentru instante
     *
     * @param nodeIndex
     * @param maxCapacity
     */
    public Node(int nodeIndex, int maxCapacity) {
        this.nodeIndex = nodeIndex;
        this.maxCapacity = maxCapacity;
        instances = new LinkedList<Instance>();
    }

    /**
     * Getter pentru lista de instante
     *
     * @return
     */
    public LinkedList<Instance> getInstances() {
        return instances;
    }

    /**
     * Getter pentru indexul nodului
     * @return
     */
    public int getNodeIndex() {
        return nodeIndex;
    }

    /**
     * Aceasta metoda adauga instanta in lista de instante din nodul curent
     * Adaugarea se face sortat inserand in lista la inceput
     * Elementele cele mai vechi
     *
     * @param instance
     */
    public void addInstance(Instance instance){
        instances.addFirst(instance);
    }

    /**
     * Aceasta metoda returneaza numarul de locuri disponibile in lista de instante
     *
     * @return
     */
    public int slotsLeft(){
        return this.maxCapacity - this.instances.size();
    }

    /**
     * Aceasta metoda returneaza true daca lista de instante este goale
     * si false in caz contrar
     *
     * @return
     */
    public boolean isEmpty(){
        return instances.isEmpty();
    }

    /**
     * Aceasta metoda returneaza true daca lista de instante este plina
     * si false in caz contrar
     *
     * @return
     */
    public boolean isFull() { return this.slotsLeft() == 0; }

    /**
     * Aceasta metoda afiseaza in fisierul de output numele nodului si informatiile
     * corespunzatoare pentru fiecare instanta din nod in ordinea dorita
     *
     * Daca scrierea in fisier esueaza se arunca exceptia IOException
     *
     * @param output
     * @throws IOException
     */
    public void display(BufferedWriter output) throws IOException {
        output.write("Nod" + this.getNodeIndex() + "\n");
        for(int i = 0; i < instances.size(); i++){
            Instance instance = instances.get(i);
            instance.display(output);
        }
    }

    /**
     * Aceasta metoda returneaza true daca nodul are in lista de instante
     * instanta care face parte din entitatea data ca parametru si cheia instantei
     *
     * Returneaza false in caz contrar
     *
     * @param entityName
     * @param key
     * @return
     */
    public boolean contains(String entityName, String key) {
        boolean contains = false;
        for(int i = 0; i < instances.size(); i++){
            Instance instance = instances.get(i);
            if(instance.getEntityName().equals(entityName) && instance.getKey().getValue().equals(key)){
                contains = true;
                break;
            }
        }
        return contains;
    }

    /**
     * Aeasta metoda cauta instanta in lista dupa entitatea din care face parte si cheie
     * Daca este gasita se returneaza referinta catre ea
     * Se returneaza null in caz contrar
     *
     * @param entityName
     * @param key
     * @return
     */
    public Instance searchInstance(String entityName, String key){
        Instance searched = null;

        for(int i = 0; i < instances.size(); i++){
            Instance instance = instances.get(i);
            if(instance.getEntityName().equals(entityName) && instance.getKey().getValue().equals(key)){
                searched = instance;
                break;
            }
        }

        return searched;
    }

    /**
     * Aceasta metoda sorteaza instantele din nod cu functia sort din
     * clasa Collections
     *
     * Se foloseste de o implementare imediat mai jos a unui Comparator
     * pe baza timestamp-ului
     */
    public void update() {
        instances.sort(new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                if(o1.getTimestamp() > o2.getTimestamp()){
                    return -1;
                }if(o1.getTimestamp() < o2.getTimestamp()){
                    return 1;
                }else{
                    return 0;
                }
            }
        });
    }

    /**
     * Aceasta metoda sterge instanta data ca parametru din nodul curent
     * (daca exista)
     *
     * Stergerea se realizeaza pe baza entitatii din care face parte si cheii
     * instantei data ca parametru
     *
     * @param oldInstance
     */
    public void remove(Instance oldInstance){
        String entityName = oldInstance.getEntityName();
        String key = oldInstance.getKey().getAttribute();

        for(int i = 0; i < instances.size(); i++){
            Instance instance = instances.get(i);
            if(instance.getEntityName().equals(entityName) && instance.getKey().getValue().equals(key)){
                instances.remove(i);
                break;
            }
        }
    }
}

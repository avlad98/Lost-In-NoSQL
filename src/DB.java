import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 *Constructor pentru baza de date. Se populeaza baza de date cu noduri goale
 * care vor contine fiecare cate o lista de instante.
 * "nodes"
 *
 * Fiecare baza de date contine o lista statica de entitati (modelele dupa care se creeaza instantele nou adaugate)
 * "entities"
 *
 * "dbName" -> numele bazei de date
 *
 * "nrNodes" -> numarul de noduri din aceasta baza de date
 *
 * "maxCapacity" -> capacitatea maxima pe care o poate avea nodul (Numarul de instante din fiecare nod)
 */
public class DB{
    private LinkedList<Node> nodes;
    private static LinkedList<Entity> entities = new LinkedList<Entity>();
    private String dbName;
    private int nrNodes;
    private int maxCapacity;

    public DB(String dbName, int nrNodes, int maxCapacity) {
        this.dbName = dbName;
        this.nrNodes = nrNodes;
        this.maxCapacity = maxCapacity;
        nodes = new LinkedList<Node>();

        //Initializare noduri goale
        for(int index = 0; index < this.nrNodes; index++){
            Node node = new Node(index+1, this.maxCapacity);
            nodes.add(node);
        }

    }

    /**
     *Aceasta metoda returneaza entitatea cautata (dupa nume)
     *
     * Se cauta in lista statica entitatea dupa numele dat ca parametru
     * Prima entitate (si unica) care face match pe nume este returnata.
     * Se returneaza null in caz contrar
     */
    public static Entity getEntity(String entityName) {
        Entity entity = null;
        for(int i = 0; i < entities.size(); i++){
            Entity tmpEntity = entities.get(i);
            if(tmpEntity.getName().equals(entityName)){
                entity = tmpEntity;
                break;
            }
        }

        return entity;
    }

    /**
     * Se adauga in lista statica din baza de date
     * entitatea data ca parametru.
     * @param entity
     */
    public void addEntity(Entity entity) {
        DB.entities.add(entity);
    }

    /**
     *Se adauga instanta in baza de date conform regulilor
     * @param instance
     */
    public void addInstance(Instance instance) {
        String entityName = instance.getEntityName();

        //Se obtine entitatea model
        Entity entity = DB.getEntity(entityName);

        //Se obtine factorul de replicare
        int RF = entity.getRF();
        int RFleft = RF;

        //Variabila care numara nodurile folosite
        int usedNodes = 0;

        boolean inserted = false;

            //Se insereaza instanta in RF noduri pana cand RFLeft devine 0
            for (int index = 0; index < nodes.size(); index++) {
                if (RFleft <= 0) {
                    inserted = true;
                    break;
                }

                Node node = nodes.get(index);
                if (node.slotsLeft() > 0 && !node.contains(instance.getEntityName(), instance.getKey().getValue())) {
                    node.addInstance(instance.clone());
                    RFleft--;
                } else {
                    continue;
                }
            }

            if(inserted == false){
                while (RFleft > 0){
                    int nextIndex = nodes.size() + 1;
                    Node node = new Node(nextIndex, this.maxCapacity);
                    node.addInstance(instance);
                    nodes.add(node);
                    RFleft--;
                    this.nrNodes++;
                }
            }
    }

    /**
     * Metoda SNAPSHOT afiseaza baza de date in starea actuala
     * Output-ul este scris in fisierul de output dorit
     *
     * In cazul in care scrierea in fisier esueaza, exceptia este aruncata
     *
     * @param output
     * @throws IOException
     */
    public void SNAPSHOTDB(BufferedWriter output) throws IOException {
        int emptyNodes = 0;

        //Se afiseaza instantele din toate nodurile
        for(int index = 0; index < nodes.size(); index++){
            Node node = nodes.get(index);

            //In cazul in care nodul curent este gol nu se afiseaza nimic
            if(node.isEmpty()){
                emptyNodes++;
                continue;
            }else{
                node.display(output);
            }
        }

        //Daca nu exista nicio instanta in niciun nod atunci se va
        //afisa "EMPTY DB" in fisierul de output
        if(emptyNodes == nodes.size()){
            output.write("EMPTY DB\n");
        }
    }

    /**
     * Aceasta metoda cauta instanta in toate nodurile bazei de date si
     * afiseaza informatiile necesare in fisierul de output
     *
     * In cazul in care scrierea in fisier nu reuseste se arunca exceptia IOException
     *
     * @param entityName
     * @param key
     * @param output
     * @param db
     * @throws IOException
     */
    public void searchInstance(String entityName, String key, BufferedWriter output, DB db) throws IOException{
        //Se obtine instanta din baza de date care are ca model entitatea X si cheia specifica instantei cautate
        Instance instance = db.getInstance(entityName, key);

        //Daca instanta nu exista in baza de date se scrie mesajul corespunzator
        if (instance == null) {
            output.write("NO INSTANCE FOUND\n");
            return;
        }

        //Se cauta in fiecare nod instanta dorita
        //Cand instanta este gasita in nodul curent se afiseaza numele nodului
        //In cazul in care instanta nu exista in nod nu se afiseaza nimic
        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);
            if(node.contains(entityName, key)){
                output.write("Nod" + node.getNodeIndex() + " ");
            }
        }

        //Dupa ce s-au afisat nodurile din care face parte instanta
        //se afiseaza si restul informatiilor despre instanta
        instance.display(output);
    }

    /**
     * Aceasta metoda cauta instanta in baza de date pe baza numelui entitatii
     * de care apartine si dupa cheia specifica instantei
     *
     * Daca instanta este gasita se returneaza referinta catre ea
     * In caz contrar se returneaza null
     *
     * @param entityName
     * @param key
     * @return
     */
    public Instance getInstance(String entityName, String key){
        Instance searchedInstance = null;

        //Se cauta in lista de noduri prima aparitie a instantei dorite
        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);

            //Se sare peste nodurile goale
            if(node.isEmpty()){
                continue;
            }else{
                //La prima aparitie a instantei se opreste cautarea si se returneaza instanta
                if(node.contains(entityName, key)) {
                    searchedInstance = node.searchInstance(entityName, key);
                    break;
                }
            }
        }

        return searchedInstance;
    }

    /**
     * Aceasta metoda actualizeaza toate atributele instantei dorite
     *
     * @param entityName
     * @param key
     * @param attributesName
     * @param attributesValue
     */
    public void update(String entityName, String key, LinkedList<String> attributesName, LinkedList<String> attributesValue) {
        //Se cauta in fiecare nod instanta
        for(int index = 0; index < nodes.size(); index++){
            Node node = nodes.get(index);

            //Se sare peste nodurile goale
            if(node.isEmpty()){
                continue;
            }else{
                //Daca nodul contine instanta specifica atunci ea se actualizeaza
                if(node.contains(entityName, key)) {
                    Instance instance = node.searchInstance(entityName, key);
                    instance.update(attributesName, attributesValue);

                    //Dupa fiecare modificare din nod se resorteaza nodul folosind
                    //metoda de sortare din clasa Collections
                    node.update();
                }
            }

        }
    }

    /**
     * Aceasta metoda sterge din baza de date toate aparitiile instantei dorite
     *
     * Daca instanta cautata nu exista in niciun nod se va afisa mesajul corespunzator
     * in fisierul de output
     *
     * In caz ca scrierea in fisier esueaza se arunca exceptia IOException
     *
     * @param entityName
     * @param key
     * @param output
     * @throws IOException
     */
    public void delete(String entityName, String key, BufferedWriter output) throws IOException{
        //Contor pentru nodurile care nu contin instanta
        int notDeleted = 0;

        //Se cauta in fiecare nod instanta
        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);

            //Se sare peste nodurile goale si se incrementeaza contorul de noduri care nu contin instanta
            if(node.isEmpty()){
                notDeleted++;
                continue;
            }else{
                //Daca nodul contine instanta atunci ea va fi stearsa si se vor sorta instantele corespunzator
                if(node.contains(entityName, key)){
                    Instance instance = node.searchInstance(entityName, key);
                    node.remove(instance);
                    node.update();
                }else{
                    //Daca nodul curent nu contine instanta atunci se incrementeaza contorul
                    notDeleted++;
                }
            }
        }

        //Atunci cand contorul ajunge egal cu numarul total de noduri din baza de date inseamna ca
        //instanta nu a fost gasita in niciun nod si se va afisa mesajul corespunzator in fisierul de output
        if(notDeleted == nodes.size()){
            output.write("NO INSTANCE TO DELETE\n");
        }
    }

    /**
     * Aceasta metoda curata din baza de date toate instantele mai vechi de un anumit timestamp
     *
     * @param timestamp
     */
    public void cleanup(long timestamp) {
        //Se cauta in fiecare nod instantele mai vechi de un anumit timestamp
        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);

            //Se sare peste nodurile goale
            if(node.isEmpty()){
                continue;
            }else{

                //Se elimina aparitiile instantei in fiecare nod dupa regula
                LinkedList<Instance> instances = node.getInstances();
                for(int j = 0; j < instances.size(); j++) {
                    Instance instance = instances.get(j);
                    if (instance.getTimestamp() < timestamp) {
                        node.remove(instance);
                        j--;
                    }
                }
            }
        }
    }
}
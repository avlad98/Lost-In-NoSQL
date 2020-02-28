import java.io.*;
import java.util.LinkedList;

public class Tema2 {

    /**
     * CREARE SI INITIALIZARE BAZA DE DATE NOSQL
     */
    public static void main(String[] args) throws Exception {
        //Open input file
        File input_file = new File(args[0]);
        BufferedReader input = new BufferedReader(new FileReader(input_file));

        //Open output file
        File output_file = new File(args[0] + "_out");
        FileWriter fw = new FileWriter(output_file.getAbsoluteFile());
        BufferedWriter output = new BufferedWriter(fw);

        //Declarare baza de date
        DB db = null;

        //Parsare input
        String line;
        while ((line = input.readLine()) != null){
            String[] parts = line.split(" ");
                String command = parts[0];
                String entityName = null;
                String key = null;
                switch (command){
                    case "CREATEDB":
                        String dbName = parts[1];
                        int nrNodes = Integer.parseInt(parts[2]);
                        int maxCapacity = Integer.parseInt(parts[3]);

                        //Creare baza de date
                        db = new DB(dbName, nrNodes, maxCapacity);
                        break;
                    case "CREATE":
                        entityName = parts[1];
                        int RF = Integer.parseInt(parts[2]);
                        int numOfAttributes = Integer.parseInt(parts[3]);
                        LinkedList<String> attributes = new LinkedList<String>();
                        LinkedList<String> attributesType = new LinkedList<String>();

                        //Atributele si tipurile de atribute sunt despartite in 2 liste
                        //Pe acelasi index se gasesc perechea atribut-tipAtribut
                        for(int i = 4; i < parts.length - 1; i += 2){
                            String attribute = parts[i];
                            attributes.add(attribute);

                            String attributeType = parts[i+1];
                            attributesType.add(attributeType);
                        }

                        //Se instantiaza entitatea
                        Entity entity = new Entity(entityName, RF, numOfAttributes, attributes, attributesType);

                        //Se adauga entitatea in baza de date
                        db.addEntity(entity);

                        break;
                    case "INSERT":
                        entityName = parts[1];
                        LinkedList<String> attributesList = new LinkedList<String>();

                        //Se stocheaza atributele intr-o lista pentru usurinta
                        for(int i = 2; i < parts.length; i++){
                            String attribute = parts[i];
                            attributesList.add(attribute);
                        }

                        //Se creeaza instanta
                        Instance instance = new Instance(entityName, attributesList);

                        //Se adauga instanta in baza de date conform regulilor
                        db.addInstance(instance);
                        break;
                    case "DELETE":
                        entityName = parts[1];
                        key = parts[2];

                        //Se sterge entitatea din baza de date pe baza tipului de entitate din care
                        //face parte si cheii specifice instantei
                        db.delete(entityName, key, output);
                        break;
                    case "UPDATE":
                        entityName = parts[1];
                        key = parts[2];
                        LinkedList<String> attributesName = new LinkedList<String>();
                        LinkedList<String> attributesValue = new LinkedList<String>();

                        //La fel ca la inserare se retin atributele si tipurile in liste
                        for(int i = 3; i < parts.length - 1; i+=2){
                            String attribute = parts[i];
                            attributesName.add(attribute);

                            String attributeValue = parts[i+1];
                            attributesValue.add(attributeValue);
                        }

                        //Baza de date actualizeaza toate instantele din toate nodurile
                        db.update(entityName, key, attributesName, attributesValue);
                        break;
                    case "GET":
                        entityName = parts[1];
                        key = parts[2];

                        //Se cauta in baza de date instantele
                        db.searchInstance(entityName, key, output, db);
                        break;
                    case "SNAPSHOTDB":

                        //Se face un snapshot
                        db.SNAPSHOTDB(output);
                        break;
                    case "CLEANUP":
                        String dbNameCleanup = parts[1];
                        long timestamp = Long.parseLong(parts[2]);

                        //Se elimina instantele mai vechi de un anumit timestamp
                        db.cleanup(timestamp);
                        break;
            }

        }

        //Close output file
        output.close();
    }
}

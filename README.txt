VLAD ANDREI-ALEXANDRU
321CB

					=============	TEMA 2	=============
								- Lost in NOSQL -
			
	
	Pentru implementarea temei am creat 6 clase publice. In toata tema am
	folosit doar LinkedList pentru a stoca obiectele intr-o structura de
	date.
	
1)	Tema2:
	
	Aceasta clasa contine doar metoda main.
	In aceasta metoda se deschide fisierul de input dat ca argument si fisierul
	de output, se parseaza liniile din fisierul de input si se executa
	instructiunile necesare.
	La finalul executarii tuturor instructiunilor se va inchide fisierul de
	output.
	
	
2)	DB:

	In aceasta clasa se intampla aproape toata gestiunea bazei de date.
	Fiecare baza de date are o lista de noduri, numele bazei de date, numarul
	de noduri si capacitatea maxima pentru fiecare nod.
	De asemenea exista si o lista statica de entitati.
	
	Constructor pentru baza de date. Se populeaza baza de date cu noduri goale
	care vor contine fiecare cate o lista de instante.
	
	Metoda "getEntity":
		Aceasta metoda returneaza entitatea cautata (dupa nume).
		Se cauta in lista statica entitatea dupa numele dat ca parametru.
		Prima entitate (si unica) care face match pe nume este returnata.
		Se returneaza null in caz contrar.
		
	Metoda "addEntity":
	    Se adauga in lista statica din baza de date entitatea data ca parametru.
		
	Metoda "addInstance":
		Se adauga instanta in baza de date conform regulilor.
		Pentru a adauga instanta este nevoie de entitatea din care face parte
		ca model si de factorul de replicare.
		
		Se obtine entitatea din care face parte instanta (folosind o alta metoda
		de cautare) si se insereaza instanta in atatea noduri in cate este nevoie
		(RF-ul este luat din entitatea corespunzatoare).
		
		Se insereaza instanta in noduri pana cand RF-ul este epuizat.
		
		Daca nu se mai poate insera in niciun nod, iar RF-ul nu este epuizat se
		vor crea noi noduri pentru fiecare RF si se fac actualizarile necesare.
		
	Metoda "SNAPSHOTDB":
		Afiseaza in fisierul de output informatiile necesare.
		Se afiseaza instantele din toate nodurile in ordinea timestamp-ului cel
		mai mare.
		
		Daca nodul curent nu contine nicio instanta atunci nu se va mai afisa
		numele lui.
		
		In caz ca baza de date este goala (niciun nod nu contine vreo instanta)
		se va afisa "EMPTY DB"
		
	Metoda "searchInstance":
		Aceasta metoda cauta instanta in baza de date si afiseaza nodurile din
		care face parte si informatiile necesare.
		
		Daca instanta nu este gasita in niciunul din noduri se va afisa mesajul
		specific.
		
	Metoda "getInstance":
		Aceasta metoda cauta instanta in lista de instante din nodul respectiv
		si o returneaza daca este gasita. In caz contrar se va returna null.
		Cautarea se face pe baza numelui entitatii din care face parte instanta
		si cheia acesteia.
		
	Metoda "update":
		Aceasta metoda actualizeaza toate instantele cu atributele date ca argument.
		Se va cauta in fiecare nod instanta, iar daca aceasta este gasita, ea va fi
		actualizata impreuna cu noul timestamp.
		
		Dupa fiecare comanda de update pe fiecare nod, se va sorta lista de entitati
		din nodul respectiv (sortarea se va face cu ajutorul comparatorului definit
		de mine).
		
	Metoda "delete":
		Aceasta metoda sterge instanta dorita din fiecare nod.
		Dupa stergerea instantei dintr-un nod, lista de instante se va sorta la fel
		ca in metoda update.
		
		Daca instanta nu se regaseste in niciun nod atunci se va afisa in fisierul
		de output mesajul corespunzator.
		
	Metoda "cleanup":
		Aceasta metoda sterge toate instantele din toate nodurile care sunt mai
		vechi de un anumit timestamp dat ca parametru.
		
		
3)	Node:
	
	Fiecare nod are ca atribute o lista de instante, un index care reprezinta
	"numele nodului" si capacitatea maxima (numarul maxim de elemente pe care
	il poate tine lista de entitati).
	
	Constructorul initializeaza campurile de mai sus impreuna cu lista de entitati.
	
	Metoda "getInstances":
		Getter pentru lista de instante.
		
	Metoda "getNodeIndex":
		Getter pentru indexul nodului.
		
	Metoda "addInstance":
		Adauga instanta la inceputul listei de instante pentru a insera ordonat.
	
	Metoda "slotsLeft":
		Returneaza numarul de locuri ramase in lista de entitati.
		
	Metoda "isEmpty":
		Returneaza true cand lista de instante este goala si false altfel.
		
	Metoda "isFull":
		Returneaza true cand lista este plina si false altfel.
		
	Metoda "display":
		Aceasta metoda afiseaza in fisierul de output informatiile necesare 
		despre fiecare instanta.
		
	Metoda "contains":
		Returneaza true daca nodul curent contine instanta cautata si false altfel.
		
	Metoda "searchInstance":
		Aceasta metoda cauta instanta dorita in lista de instante si o returneaza.
		Daca aceasta nu este gasita se intoarce null.
		
	Metoda "update":
		Aceasta metoda sorteaza lista de instante pe baza sortarii din clasa
		"Collections". Pentru sortare se foloseste un comparator (pe baza 
		timestamp-ului) definit de mine.
		
	Metoda "remove":
		Aceasta metoda sterge instanta dorita din lista de instante.
		
		
4)	Entity:

	Aceasta clasa contine o lista de atribute (numele acestora), o lista de
	tipuri de atribute, un nume pentru entitate, un factor de multiplicare (RF)
	si un numar de atribute.
	Am impartit atributul in 2 liste pentru simplitate. Pe acelasi index din 
	cele 2 liste se	gaseste informatia pentru un singur atribut.
	
	Metodele acestei clase sunt gettere pentru campurile de mai sus.
	
	
5)	Instance:

	Aceasta clasa contine o lista de atribute, numele entitatii din care face
	parte, un timestamp si o cheie.
	
	Constructorul acestei clase initializeaza campurile de mai sus, iar in
	lista de atribute se insereaza atributele care se creaza in "for".
	Se foloseste entitatea ca model pentru atribute.
	Cheia fiecarei instante va fi primul element din lista de atribute.
	
	Metoda "display":
		Aceasta metoda afiseaza informatiile necesare in fisierul de output.
		(numele entitatii si atributele specifice)
	
	Metoda "update":
		Aceasta metoda actualizeaza toate atributele din lista de atribute pe
		baza noilor argumente primite ca argument (intr-o lista) si se
		reinitializeaza cheia cu primul element din lista actualizata.
		De asemenea, timestamp-ul va fi actualizat la fiecare update.
		
	Metoda "compareTo":
		Aceasta metoda este suprascrisa pentru a fi folosita de sortarea din
		"Collections". Se va returna 1, 0 sau -1 in functie de timestamp-ul
		instantelor care se compara.
		
	Metoda "clone":
		Aceasta metoda este suprascrisa pentru a face deep clone pe fiecare
		instanta. Timestamp-ul clonei este de asemenea actualizat, iar lista
		de atribute este si ea clonata.
		
	Restul metodelor nementionate sunte gettere si settere.
	

6)	Attribute:

	Aceasta clasa contine campuri pentru un int, un float, un string, tipul
	atributului si "numele"/valoarea(in String) atributului.
	
	Constructorul acestei clase initializeaza campurile specifice in functie
	de tipul atributului.
	
	Metoda "clone":
		Este de asemenea suprascrisa si cloneaza atributul curent.
		
	Metoda "getValue":
		Aceasta metoda returneaza valoarea atributului in string. Aceasta
		transformare in string se face, de asemenea, pe baza tipului
		atributului.
		
	Metoda "update":
		Aceasta metoda actualizeaza continutul atributului in functie de tipul
		acestuia.
		
	Restul metodelor sunt gettere si settere.
	

PUNCTAJ:	
	Pe masina locala obtin 120 puncte.
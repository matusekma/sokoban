package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * tesztel� oszt�ly a viselked�sek bemutat�s�ra
 */
public class Tester {
	Outputter op;
	Game game;
	Maze m;
	PrintWriter printer = null;
	boolean stdout = false;
	
	ArrayList<Worker> workers;
	ArrayList<Box> boxes;				//az elt�rolt l�d�k, munk�sok �s mez�k
	ArrayList<FieldBase> fields;
	FieldBase fieldMap[][];
	
	
	
	public Tester() {
		workers = new ArrayList<Worker>();
		boxes = new ArrayList<Box>();
		fields = new ArrayList<FieldBase>();
	} 
	

	/**
	 * a megadott parancsot feldolgozza
	 * @param command a parancs
	 * @param fileName a bemeneti f�jl
	 * @return kil�p�nk-e
	 */
	public boolean processCommand(String command, String fileName) {
		if(fileName != null)
			try {
				printer = new PrintWriter(new FileWriter(fileName + "_result.txt", true));		//ide �rjuk a kimenetet
				stdout = false;
			} catch (FileNotFoundException e) {
				System.out.println("Nem l�tezik a megadott f�jl.");
				return false;
			}
			catch (IOException e) {
				return false;
			}
		else
			stdout = true;
		String parts[] = command.split(" ");
		switch (parts[0]) {
			case "loadtest":							//tesztp�lya bet�lt�se �s a rakt�r�p�let ki�r�sa
				loadtestCommand(parts);
				if(fileName == null) System.out.println("A bet�lt�tt tesztp�lya:");
				showmazeCommand(printer);
				break;
		
			case "move":										
				moveCommand(parts);
				break;
			
			case "put":
				putCommand(parts);
				break;	
			case "listboxes":									
				for(Box b : boxes)
					b.printState(printer, stdout);
				break;
			
			case "listworkers":									
				for(Worker w : workers)
					w.printState(printer, stdout);
				break;
			
			case "showstate":									
				showstateCommand(parts);
				break;
			
			case "showmaze":									
					showmazeCommand(printer);
				break;
			case "exit":
				if(printer != null)printer.close();
				return true;
			default:
				System.out.println("Nincs ilyen parancs.");
		}
		if(printer!=null) printer.close();
		return false; 
	}
		
	/**
	 * kimenet �s v�rt kiemenet �sszahasonl�t�sa
	 * @param patha kapott kimeneti f�jl
	 * @param pathb elv�rt kimeneti f�jl
	 */
	public void compareWithExpected(String patha, String pathb)
	{
		int i = 1;
		File a = new File(patha);
		File b = new File(pathb);
		BufferedReader readera = null;
		BufferedReader readerb = null;
		try {
			readera = new BufferedReader(new FileReader(a));
			readerb = new BufferedReader(new FileReader(b));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String linea = null;
		String lineb = null;
		boolean hibas = false;
		try 
		{
			while ((linea = readera.readLine()) != null && (lineb = readerb.readLine())!= null) 
			{
				if (!linea.equals(lineb))
				{
					hibas = true;
					System.out.println(i + ". sor hibas");		
				}

				i++;
			}
			if (!hibas)
				System.out.println("Hiba n�lkul futott le a teszteset, a kimenet megegyezik az elv�rttal " + "(lsd. " + pathb + ")");
			readera.close();
			readerb.close();
		}
		catch (IOException ex)
		{
			//
		}
	
	}

	public void clearTest() {
		workers.clear();
		boxes.clear();
		fields.clear();
	}
	
	
	public void initTest1(){
		fieldMap = new FieldBase[1][2];
		
		Worker w = new Worker("w", 10);
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		
		fields.add(f1);
		fields.add(f2);
		workers.add(w);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		
		f1.setNeighbor(Direction.Right, f2);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f1.setThing(w);
		w.setField(f1);
	}
	
	public void initTest2(){
		fieldMap = new FieldBase[1][2];
		
		Worker w = new Worker("w", 10);
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Hole("hole");
		
		fields.add(f1);
		fields.add(f2);
		workers.add(w);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		
		f1.setNeighbor(Direction.Right, f2);
		f1.setFriction(Friction.Normal);
		f1.setThing(w);
		w.setField(f1);
	}
	
	public void initTest3(){
		fieldMap = new FieldBase[1][2];
		
		Worker w = new Worker("w", 10);
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new SwitchableHole("shole");
		
		fields.add(f1);
		fields.add(f2);
		workers.add(w);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		
		f1.setNeighbor(Direction.Right, f2);
		f1.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f1.setThing(w);
		w.setField(f1);
	}
	
	public void initTest4(){
		initTest3();
		((SwitchableHole)fields.get(1)).setOpen();
	}
	
	public void initTest5(){
		fieldMap = new FieldBase[1][2];
		
		Worker w = new Worker("w", 10);
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Obstacle("obs");
		
		fields.add(f1);
		fields.add(f2);
		workers.add(w);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		
		f1.setNeighbor(Direction.Right, f2);
		f1.setFriction(Friction.Normal);
		f1.setThing(w);
		
		w.setField(f1);
	}
	
	public void initTest6() {
		fieldMap = new FieldBase[1][4];
		
		Worker w1 = new Worker("w1", 10);
		Worker w2 = new Worker("w2", 10);
		Box b = new Box("box");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new Field("f3");
		FieldBase f4 = new Field("f4");
		
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		fields.add(f4);
		workers.add(w1);
		workers.add(w2);
		boxes.add(b);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		fieldMap[0][2] = f3;
		fieldMap[0][3] = f4;
		
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f3.setNeighbor(Direction.Right, f4);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		f4.setFriction(Friction.Normal);
		f1.setThing(w1);
		w1.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
		
		f3.setThing(w2);
		w2.setField(f3);
	}
	
	public void initTest7() {
		//fieldMap = new FieldBase[1][3];
		
		Worker w1 = new Worker("w1", 10);
		Worker w2 = new Worker("w2", 10);
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		//FieldBase f3 = new Field("f3");
		
		fields.add(f1);
		fields.add(f2);
		//fields.add(f3);
		workers.add(w1);
		workers.add(w2);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		//fieldMap[0][2] = f3;
		
		f1.setNeighbor(Direction.Right, f2);
		//f2.setNeighbor(Direction.Right, f3);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		//f3.setFriction(Friction.Normal);
		f1.setThing(w1);
		w1.setField(f1);
		f2.setThing(w2);
		w2.setField(f2);
	}
	
	
	
	public void initTest8() {
		fieldMap = new FieldBase[1][3];
		
		Worker w = new Worker("w", 10);
		Box b = new Box("box");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new Field("f3");
		
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		workers.add(w);
		boxes.add(b);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		fieldMap[0][2] = f3;
		
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		f1.setThing(w);
		w.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
	}
	
	public void initTest9() {
		fieldMap = new FieldBase[1][3];
		
		Worker w = new Worker("w", 10);
		Box b = new Box("box");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		BoxField f3 = new BoxField("bf");
		
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		workers.add(w);
		boxes.add(b);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		fieldMap[0][2] = f3;
		
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		f1.setThing(w);
		w.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
		
		f3.setOwner(w);
		
		b.setOwner(w);
		w.AddPoints(1);
	}
	
	public void initTest10() {
		initTest9();
		boxes.get(0).setOwner(null);
	}
	
	public void initTest11(){
		fieldMap = new FieldBase[1][4];
		
		Worker w1 = new Worker("w1", 10);
		Worker w2 = new Worker("w2", 10);
		Box b = new Box("box");
		FieldBase f0 = new Field("f0");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		BoxField f3 = new BoxField("bf");
		
		fields.add(f0);
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		workers.add(w1);
		workers.add(w2);
		boxes.add(b);
		
		fieldMap[0][0] = f0;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = f3;
		
		f0.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f0.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		
		w1.AddPoints(1);
		f0.setThing(w1);
		w1.setField(f0);
		
		f1.setThing(w2);
		w2.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
		
		f3.setOwner(w1);
		
		b.setOwner(w1);
	}
	
	public void initTest12() {
		fieldMap = new FieldBase[1][3];
		
		Worker w = new Worker("w", 10);
		Box b = new Box("box");
		FieldBase f1 = new Field("f1");
		BoxField bf = new BoxField("bf");
		FieldBase f2 = new Field("f2");
		
		
		fields.add(f1);
		fields.add(bf);
		fields.add(f2);
		
		workers.add(w);
		boxes.add(b);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = bf;
		fieldMap[0][2] = f2;
		
		f1.setNeighbor(Direction.Right, bf);
		bf.setNeighbor(Direction.Right, f2);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		bf.setFriction(Friction.Normal);
		
		f1.setThing(w);
		w.setField(f1);
		w.AddPoints(1);
		
		bf.setThing(b);
		bf.setBox(b);
		b.setField(bf);
		
		b.setOwner(w);
		
		bf.setOwner(w);
	
		b.setOwner(w);
	}
	
	public void initTest13() {
		fieldMap = new FieldBase[1][4];
		
		Worker w1 = new Worker("w1", 10);
		Worker w2 = new Worker("w2", 10);
		Box b = new Box("box");
		FieldBase f0 = new Field("f0");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		BoxField bf = new BoxField("bf");
		
		fields.add(f0);
		fields.add(f1);
		fields.add(bf);
		fields.add(f2);
		
		workers.add(w1);
		workers.add(w2);
		boxes.add(b);
		
		fieldMap[0][0] = f0;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = bf;
		fieldMap[0][3] = f2;
		
		f0.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, bf);
		bf.setNeighbor(Direction.Right, f2);
		f0.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		bf.setFriction(Friction.Normal);
		
		f0.setThing(w1);
		w1.setField(f0);
		
		f1.setThing(w2);
		w2.setField(f1);
		
		bf.setBox(b);
		bf.setThing(b);
		b.setField(bf);
		
		bf.setOwner(w1);
		
		b.setOwner(w1);
		w1.AddPoints(1);
	}
	
	public void initTest14() {
		fieldMap = new FieldBase[1][5];
		
		Worker w1 = new Worker("w1", 10);
		Worker w2 = new Worker("w2", 10);
		Box b1 = new Box("box1");
		Box b2 = new Box("box2");
		FieldBase f0 = new Field("f0");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new Field("f3");
		BoxField bf = new BoxField("bf");
		
		fields.add(f0);
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		fields.add(bf);
		
		workers.add(w1);
		workers.add(w2);
		boxes.add(b1);
		boxes.add(b2);
		
		fieldMap[0][0] = f0;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = f3;
		fieldMap[0][4] = bf;
		
		f0.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f3.setNeighbor(Direction.Right, bf);
		f0.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		bf.setFriction(Friction.Normal);
		
		f0.setThing(w1);
		w1.setField(f0);
		
		f1.setThing(b1);
		b1.setField(f1);
		
		f2.setThing(w2);
		w2.setField(f2);
		
		f3.setThing(b2);
		b2.setField(f3);
	}
	
	public void initTest15() {
		fieldMap = new FieldBase[1][3];
		
		Worker w = new Worker("w", 10);
		Box b = new Box("box");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		Hole h = new Hole("h");
		
		fields.add(f1);
		fields.add(f2);
		fields.add(h);
		workers.add(w);
		boxes.add(b);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		fieldMap[0][2] = h;
		
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, h);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f1.setThing(w);
		w.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
	}
	
	public void initTest16() {
		fieldMap = new FieldBase[1][3];
		
		Worker w = new Worker("w", 10);
		Box b = new Box("box");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		SwitchableHole shole = new SwitchableHole("shole");
		
		fields.add(f1);
		fields.add(f2);
		fields.add(shole);
		workers.add(w);
		boxes.add(b);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		fieldMap[0][2] = shole;
		
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, shole);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f1.setThing(w);
		w.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
	}
	
	public void initTest17() {
		initTest16();
		SwitchableHole sh = (SwitchableHole) fields.get(2);
		sh.setOpen();
	}
	
	public void initTest18() {
		fieldMap = new FieldBase[1][4];
		
		Worker w = new Worker("w", 10);
		Box b = new Box("box");
		SwitchableHole shole = new SwitchableHole("shole");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		Switch sw = new Switch("sw");
		
		sw.SetSh(shole);
		
		fields.add(shole);
		fields.add(f1);
		fields.add(f2);
		fields.add(sw);
		
		workers.add(w);
		boxes.add(b);
		
		fieldMap[0][0] = shole;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = sw;
		
		shole.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, sw);
		
		shole.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		sw.setFriction(Friction.Normal);
		
		f1.setThing(w);
		w.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
	}
	
	public void initTest19() {
		fieldMap = new FieldBase[1][4];
		
		Worker w = new Worker("w", 10);
		Box b = new Box("box");
		SwitchableHole shole = new SwitchableHole("shole");
		FieldBase f1 = new Field("f1");
		Switch sw = new Switch("sw");
		FieldBase f2 = new Field("f2");
		
		sw.SetSh(shole);
		
		fields.add(shole);
		fields.add(f1);
		fields.add(sw);
		fields.add(f2);
		
		workers.add(w);
		boxes.add(b);
		
		fieldMap[0][0] = shole;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = sw;
		fieldMap[0][3] = f2;
		
		
		shole.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, sw);
		sw.setNeighbor(Direction.Right, f2);
		
		shole.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		sw.setFriction(Friction.Normal);
		
		f1.setThing(w);
		w.setField(f1);
		
		sw.setThing(b);
		sw.setBox(b);
		b.setField(sw);
		
		sw.Change();
	}
	
	public void initTest20() {
		fieldMap = new FieldBase[1][3];
		
		Worker w = new Worker("w", 10);
		Box b = new Box("box");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		Obstacle f3 = new Obstacle("obs");
		
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		workers.add(w);
		boxes.add(b);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		fieldMap[0][2] = f3;
		
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		f1.setThing(w);
		w.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
	}
	
	public void initTest21() {
		fieldMap = new FieldBase[1][4];
		
		Worker w = new Worker("w", 10);
		Box b1 = new Box("box1");
		Box b2 = new Box("box2");
		FieldBase f0 = new Field("f0");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new Field("f3");
		
		fields.add(f0);
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		workers.add(w);
		boxes.add(b1);
		boxes.add(b2);
		
		fieldMap[0][0] = f0;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = f3;
		
		f0.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f0.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		
		f0.setThing(w);
		w.setField(f0);
		
		f1.setThing(b1);
		b1.setField(f1);
		
		f2.setThing(b2);
		b2.setField(f2);
	}
	
	public void initTest22() {
		fieldMap = new FieldBase[1][4];
		
		Worker w = new Worker("w", 10);
		Box b1 = new Box("box1");
		Box b2 = new Box("box2");
		FieldBase f0 = new Field("f0");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new BoxField("bf");
		
		fields.add(f0);
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		workers.add(w);
		boxes.add(b1);
		boxes.add(b2);
		
		fieldMap[0][0] = f0;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = f3;
		
		f0.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f0.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		
		f0.setThing(w);
		w.setField(f0);
		
		f1.setThing(b1);
		b1.setField(f1);
		
		f2.setThing(b2);
		b2.setField(f2);
	}
	
	public void initTest23() {
		fieldMap = new FieldBase[1][5];
		
		Worker w = new Worker("w", 10);
		Box b1 = new Box("box1");
		Box b2 = new Box("box2");
		Box b3 = new Box("box3");
		FieldBase f0 = new Field("f0");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new Field("f3");
		FieldBase f4 = new Field("f4");
		
		fields.add(f0);
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		fields.add(f4);
		workers.add(w);
		boxes.add(b1);
		boxes.add(b2);
		boxes.add(b3);
		
		fieldMap[0][0] = f0;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = f3;
		fieldMap[0][4] = f4;
		
		f0.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f3.setNeighbor(Direction.Right, f4);
		f0.setFriction(Friction.Normal);
		f1.setFriction(Friction.Honey);
		f2.setFriction(Friction.Oil);
		f3.setFriction(Friction.Normal);
		f4.setFriction(Friction.Normal);
		
		f0.setThing(w);
		w.setField(f0);
		
		f1.setThing(b1);
		b1.setField(f1);
		
		f2.setThing(b2);
		b2.setField(f2);
		
		f3.setThing(b3);
		b3.setField(f3);
	}
	
	public void initTest24() {
		fieldMap = new FieldBase[1][5];
		
		Worker w = new Worker("w", 10);
		Box b1 = new Box("box1");
		Box b2 = new Box("box2");
		Box b3 = new Box("box3");
		FieldBase f0 = new Field("f0");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new Field("f3");
		FieldBase f4 = new Field("f4");
		
		fields.add(f0);
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		fields.add(f4);
		workers.add(w);
		boxes.add(b1);
		boxes.add(b2);
		boxes.add(b3);
		
		fieldMap[0][0] = f0;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = f3;
		fieldMap[0][4] = f4;
		
		f0.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f3.setNeighbor(Direction.Right, f4);
		f0.setFriction(Friction.Normal);
		f1.setFriction(Friction.Honey);
		f2.setFriction(Friction.Honey);
		f3.setFriction(Friction.Normal);
		f4.setFriction(Friction.Normal);
		
		f0.setThing(w);
		w.setField(f0);
		
		f1.setThing(b1);
		b1.setField(f1);
		
		f2.setThing(b2);
		b2.setField(f2);
		
		f3.setThing(b3);
		b3.setField(f3);
	}
	
	public void initTest25() {
		fieldMap = new FieldBase[1][4];
		
		Worker w1 = new Worker("w1", 10);
		Worker w2 = new Worker("w2", 10);
		Box b = new Box("box");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new Field("f3");
		FieldBase f4 = new Obstacle("obs");
		
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		fields.add(f4);
		workers.add(w1);
		workers.add(w2);
		boxes.add(b);
		
		fieldMap[0][0] = f1;
		fieldMap[0][1] = f2;
		fieldMap[0][2] = f3;
		fieldMap[0][3] = f4;
		
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f3.setNeighbor(Direction.Right, f4);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);

		f1.setThing(w1);
		w1.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
		
		f3.setThing(w2);
		w2.setField(f3);
	}
	
	
	public void initTest26() {
		fieldMap = new FieldBase[1][1];
		
		Worker w = new Worker("w", 10);
		FieldBase f1 = new Field("f1");
		
		fields.add(f1);
		workers.add(w);
		
		fieldMap[0][0] = f1;
	
		f1.setFriction(Friction.Normal);
		
		f1.setThing(w);
		w.setField(f1);
	}
	
	public void initTest27() {
		fieldMap = new FieldBase[1][5];
		
		Worker w1 = new Worker("w1", 10);
		Worker w2 = new Worker("w2", 10);
		Worker w3 = new Worker("w3", 10);
		Box b = new Box("box");
		FieldBase f0 = new Field("f0");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		FieldBase f3 = new Field("f3");
		FieldBase f4 = new Field("f4");
		
		fields.add(f0);
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		fields.add(f4);
		workers.add(w1);
		workers.add(w2);
		workers.add(w3);
		boxes.add(b);
		
		fieldMap[0][0] = f0;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = f3;
		fieldMap[0][4] = f4;
		
		f0.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, f3);
		f3.setNeighbor(Direction.Right, f4);
		f0.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		f3.setFriction(Friction.Normal);
		f4.setFriction(Friction.Normal);
		
		f0.setThing(w1);
		w1.setField(f0);
		
		f1.setThing(b);
		b.setField(f1);
		
		f2.setThing(w2);
		w2.setField(f2);
		
		f3.setThing(w3);
		w3.setField(f3);
	}
	public void initTest28() {
		fieldMap = new FieldBase[1][4];
		
		Worker w1 = new Worker("w1", 10);
		Worker w2 = new Worker("w2", 10);
		Box b = new Box("box");
		SwitchableHole shole = new SwitchableHole("shole");
		FieldBase f1 = new Field("f1");
		FieldBase f2 = new Field("f2");
		Switch sw = new Switch("sw");
		
		sw.SetSh(shole);
		
		fields.add(shole);
		fields.add(f1);
		fields.add(f2);
		fields.add(sw);
		
		workers.add(w1);
		workers.add(w2);
		boxes.add(b);
		
		fieldMap[0][0] = shole;
		fieldMap[0][1] = f1;
		fieldMap[0][2] = f2;
		fieldMap[0][3] = sw;
		
		shole.setNeighbor(Direction.Right, f1);
		f1.setNeighbor(Direction.Right, f2);
		f2.setNeighbor(Direction.Right, sw);
		
		shole.setFriction(Friction.Normal);
		f1.setFriction(Friction.Normal);
		f2.setFriction(Friction.Normal);
		sw.setFriction(Friction.Normal);
		
		shole.setThing(w2);
		w2.setField(shole);
		
		f1.setThing(w1);
		w1.setField(f1);
		
		f2.setThing(b);
		b.setField(f2);
	}
	
	/**
	 * teszt bet�lt�se
	 */
	private void loadtestCommand(String[] parts) {
		clearTest();
		int test = Integer.parseInt(parts[1]);
		switch (test) {
			case 1:
				initTest1();
				break;
			case 2:
				initTest2();
				break;
			case 3:
				initTest3();
				break;
			case 4:
				initTest4();
				break;
			case 5:
				initTest5();
				break;
			case 6:
				initTest6();
				break;
			case 7:
				initTest7();
				break;
			case 8:
				initTest8();
				break;
			case 9:
				initTest9();
				break;
			case 10:
				initTest10();
				break;
			case 11:
				initTest11();
				break;
			case 12:
				initTest12();
				break;
			case 13:
				initTest13();
				break;
			case 14:
				initTest14();
				break;
			case 15:
				initTest15();
				break;
			case 16:
				initTest16();
				break;
			case 17:
				initTest17();
				break;
			case 18:
				initTest18();
				break;
			case 19:
				initTest19();
				break;
			case 20:
				initTest20();
				break;
			case 21:
				initTest21();
				break;
			case 22:
				initTest22();
				break;
			case 23:
				initTest23();
				break;	
			case 24:
				initTest24();
				break;
			case 25:
				initTest25();
				break;
			case 26:
				initTest26();
				break;	
			case 27:
				initTest27();
				break;	
			case 28:
				initTest28();
				break;	
		}
	}
	
	/**
	 * "Kirajzolja" a rakt�r�p�letet
	 */
	private void showmazeCommand(PrintWriter printer) {
		for(int i = 0; i < fieldMap.length; ++i) {
			for(int j = 0; j < fieldMap[i].length; ++j) {
				FieldBase f = fieldMap[i][j];
				if(stdout)
					System.out.print(f + ":" + f.getThing()+ " ");
				else
					printer.print(f + ":" + f.getThing()+ " ");
			}
			if(stdout)
				System.out.println("");
			else
				printer.println("");
			
		}
		if(stdout)
			System.out.println("");
		else
			printer.println("");
	}

	/**
	 * Munk�s mozgat�sa
	 */
	public void moveCommand(String[] parts) {
		if (parts[1] == null) {
			System.out.println("Hibas parancs.");
			return;
		}
		String workerName = parts[1];
		String direction = parts[2];
		if(parts[2] != null) {
			for(Worker w : workers) {
				if(w.name.equals(workerName)) {
					switch(direction) {
						case "right":
							w.Move(Direction.Right);
							break;
						case "left":
							w.Move(Direction.Left);
							break;
						case "up":
							w.Move(Direction.Up);
							break;
						case "down":
							w.Move(Direction.Down);
							break;
					}
				}
			}
		}
			
	}
	
	/**
	 * M�z vagy olaj lerak�sa
	 */
	public void putCommand(String[] parts) {
		if (parts[1] == null) {
			System.out.println("Hibas parancs.");
			return;
		}
		String workerName = parts[1];
		String friction = parts[2];
		if(parts[2] != null) {
			for(Worker w : workers) {
				if(w.name.equals(workerName)) {
					switch(friction) {
						case "oil":
							w.put(Friction.Oil);
							break;
						case "honey":
							w.put(Friction.Honey);
							break;
						case "normal":
							w.put(Friction.Normal);
							break;
					}
				}
			}
		}
		
	}
	
	/**
	 * �llapot ki�r�sa
	 */
	public void showstateCommand(String[] parts) {
		if (parts[1] == null) {
			System.out.println("Hibas parancs.");
			return;
		}
			
		for (FieldBase f : fields) {
			if (f.name.equals(parts[1])) {
				f.printState(printer, stdout);
				return;
			}
		}
		
		for (Box b : boxes) {
			if (b.name.equals(parts[1])) {
				b.printState(printer, stdout);
				return;
			}
		}
		
		for (Worker w : workers) {
			if (w.name.equals(parts[1])) {
				w.printState(printer, stdout);
				return;
			}
		}
		
	}
}
package sokoban;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(propOrder = { "name", "d", "field", "points" })
@XmlRootElement
public class Worker extends Thing implements Steppable {
	
	
	@XmlAttribute
	private int points;
	
	
	public Worker(String n, float pf) {
		name = n;
		points = 0;
		pushForce = pf;
	}
	
	public Worker() {
		points = 0;
		pushForce = 10;
	}
	
	//a munk�s a megadott ir�nyban l�v� mez�re mozog
	public boolean Move(Direction dir) {
		FieldBase f1 = getField().getNeighbor(dir);
		setDirection(dir);
		FieldBase old = getField();
		if (f1.Accept(this))
		{
			old.Remove();
			f1.setThing(this);
			this.setField(f1);
		}
		return false;
	}
	
	//a munk�s p pontot kap
	
	public void AddPoints(long p) {
		points += p;
	}
	
	//a munk�s �tk�zik a t dologgal
	public void CollideWith(Thing t) {
		t.HitBy(this, getDirection(), this.getPushForce());
	}
	
	//a munk�snak dir ir�nyban nekimegy egy m�sik munk�s
	public void HitBy(Worker w, Direction dir, float force) {
			
	}
	//a munk�snak dir ir�nyban nekimegy egy doboz
	public void HitBy(Box b, Direction dir, float force) {
		float tempPushForce = this.getPushForce(); //am�g tolj�k, addig m�s a pushForce
		this.setPushForce(force);  //ezzel az er�vel tol tov�bb
		setDirection(dir);
		FieldBase f = getField().getNeighbor(dir);
		FieldBase old = getField();
		boolean isAccepted = f.Accept(this);
		if (!isAccepted && f.getThing() == null) {		//Obstaclenek toltuk, ez�rt a munk�s meghal
			this.Delete();
		}
		else if(isAccepted) {
			old.Remove();
			f.setThing(this);
			this.setField(f);
		}		
		this.setPushForce(tempPushForce);  //tol�s ut�n vissza�ll�tjuk
	}
	
	public void put(Friction f) {
		this.getField().setFriction(f);
	}
	
	//a munk�s t�rl�dik a mez�j�r�l
	public void Delete() {
		getField().setThing(null);
		setField(null);
	}
	
	// a munk�s l�p
	public void Step() {
		// getteljuk az inputot, de ez meg nem kell (AFAIK)
	}

	
	@Override
	Worker Notify() {
		return this;
	}
	
	public void printState(PrintWriter w, boolean stdout) {
		if(stdout) {
			System.out.println("name:"+ name + "\n"
					+  "field:" + getField() + "\n"
					+ "direction:" + getDirection() + "\n"
					+ "points:" + points + "\n"
					+ "pushforce:" + pushForce + "\n"
					);
		}
		else
			w.println("name:"+ name + "\r\n"
					+  "field:" + getField() + "\r\n"
					+ "direction:" + getDirection() + "\r\n"
					+ "points:" + points + "\r\n"
					+ "pushforce:" + pushForce + "\r\n"
					);
	}
}

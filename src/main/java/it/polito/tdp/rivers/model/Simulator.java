package it.polito.tdp.rivers.model;


import java.util.List;
import java.util.PriorityQueue;


import it.polito.tdp.rivers.model.Event.EventType;

public class Simulator {
	//parametri della simulazione
	private double fOut;
	private double Q;
	private double irrigazione=0.05;
	
	//valori in uscita (statistiche)
	private int ggNonGarantiti;
	private double sommaC;
	private int nGiorni;
	private double mediaC=9;
	
	//stato del mondo
	private double C;
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	
	public void init(double k, River r) {
		queue=new PriorityQueue<Event>();
		this.fOut=r.getFlowAvg() * 0.8;
		this.Q=k * r.getFlowAvg() * 30;
		this.C=Q/2;
		caricaEventi(r.getFlows());
		ggNonGarantiti=0;
		nGiorni=0;
		sommaC=0;
	}

	private void caricaEventi(List<Flow> flows) {
		for(Flow f : flows) {
			queue.add(new Event(f.getDay(), EventType.F_IN, f.getFlow()));
			double acqua=0;
			double i=Math.random();
			if(i<=irrigazione)
				acqua=fOut * 10;
			else 
				acqua=fOut;

			queue.add(new Event(f.getDay(), EventType.F_OUT, acqua));
		}
		
	}
	
	public int run() {
		while(!queue.isEmpty()) {
			Event e=queue.poll();
			processaEvento(e);
			nGiorni++;
		}
		this.mediaC=sommaC/nGiorni;
		return ggNonGarantiti;
	}

	private void processaEvento(Event e) {
		switch (e.getTipo()) {
		case F_IN:
			C= C+e.getqAcqua();
			if(C>Q) {    //tracimazione
				C=Q;
			}
			break;
		case F_OUT:
			C= C-e.getqAcqua();
			if(C<=0) {   //acqua insufficiene
				C=0;
				ggNonGarantiti++;
			}
			sommaC+=C;
			break;
		}
	}
	
	public double getMediaC() {
		return this.mediaC;
	}
	
}

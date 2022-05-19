package it.polito.tdp.rivers.model;

import java.time.LocalDate;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		F_IN,
		F_OUT
	}
	
	private LocalDate data;
	private EventType tipo;
	private double qAcqua;
	
	public Event(LocalDate data, EventType tipo, double acqua) {
		this.data=data;
		this.tipo=tipo;
		this.qAcqua=acqua;
	}
	
	
	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}
	
	public double getqAcqua() {
		return qAcqua;
	}


	public void setqAcqua(double qAcqua) {
		this.qAcqua = qAcqua;
	}

	@Override
	public int compareTo(Event o) {
		if(!this.data.equals(o.data)){   //se la data è diversa ordino per data
			return this.data.compareTo(o.data);
		} else {
			if(o.getTipo().compareTo(EventType.F_OUT)==0 ) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	

}

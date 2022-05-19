package it.polito.tdp.rivers.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	
	private RiversDAO dao;
	private Map<Integer, River> idMap;
	private Simulator sim;
	
	public Model() {
		dao=new RiversDAO();
		idMap=new HashMap<Integer, River>();
		sim=new Simulator();
	}

	public List<River> getAllRivers() {
		for(River r:dao.getAllRivers()) {
			idMap.put(r.getId(), r);
		}
		for(River r:idMap.values()) {
			List<Flow> flows=dao.getFlowRiver(r.getId(), idMap);
			r.setFlows(flows);
			int nMisure=0;
			double totale=0;
			for(Flow f:flows) {
				nMisure++;
				totale+=f.getFlow();
			}
			Double fMed=totale/nMisure;		
			r.setFlowAvg(fMed);
		}
		return new LinkedList<River>( idMap.values());
	}
	
	public int simula(double k, River r) {
		sim.init(k, r);
		return sim.run();
	}
	
	public double getMediaC() {   //passacarte
		return sim.getMediaC();
	}
}

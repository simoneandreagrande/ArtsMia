package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> graph;
	private List<ArtObject> allNodes;
	private ArtsmiaDAO dao;
	  
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.allNodes = new ArrayList<>();
		this.dao = new ArtsmiaDAO();
		this.idMap = new HashMap<>();
	}
	
	
	private void loadNodes(){
		// prendo tutti oggetti da DB
		if(this.allNodes.isEmpty())
			this.allNodes = this.dao.listObjects();
		
		// ciclo su tutti i nodi e creo una mappa dove associo ad ogni id un oggetto
		// l'oggetto corrispondente, serve per portarci dietro una relazione tra gli oggetti e gli id
		if(this.idMap.isEmpty())
			for (ArtObject a : this.allNodes)
				this.idMap.put(a.getId(), a);
		
	}
	
	public void buildGraph() {
		
		
		loadNodes();
		
		Graphs.addAllVertices(this.graph, allNodes);
		
		// se il database è piccolo, può andar bene
		
//		for (ArtObject a1 : this.allNodes) {
//			for (ArtObject a2 : this.allNodes) {
//				int peso = this.dao.getWeight(a1.getId(), a2.getId());
//				Graphs.addEdgeWithVertices(this.graph, a1, a2, peso);
//			}
//		}
		
		// se il database è grande
		
		List<edgeModel> allEdges = this.dao.getAllWeights(idMap);
		
		for (edgeModel edgeI : allEdges) {
			Graphs.addEdgeWithVertices(this.graph, edgeI.getSource(), edgeI.getTarget(), edgeI.getPeso());
		}
		
		System.out.println("This graph contains " + this.graph.vertexSet().size() + " nodes.");
		System.out.println("This graph contains " + this.graph.edgeSet().size() + " edges.");
		
	}
		
	
	
	public boolean isIdinGraph (Integer objId) {
		if (this.idMap.get(objId) != null)
			return true;
		else
			return false;
	}
	
	public int calcolaConnessa (Integer objId) {
		
		// esplorare tutto il grafo
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> iterator =
				new DepthFirstIterator<>(this.graph, this.idMap.get(objId));
		
		
		List<ArtObject> compConnessa = new ArrayList<>();
		
		while (iterator.hasNext()) {
			compConnessa.add(iterator.next());
		}
		
		// prende un nodo, mi dice la dimensione della componente connessa
		// come parametri, vuole in entrata il source
		// creiamo oggetto connectivityinspector
		// che crea variabile inspector, prende argomento un nodo
		// e ti restituisce un set con tutti i nodi collegati a lui (come esplora il grafo
		// ma in più righe di codice)
		
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(this.graph);
		Set<ArtObject> setConnesso = inspector.connectedSetOf(this.idMap.get(objId));
				
//		return compConnessa.size();
		return setConnesso.size();
		
		
	}
}
	

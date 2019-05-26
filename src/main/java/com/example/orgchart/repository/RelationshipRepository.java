package com.example.orgchart.repository;

import com.example.orgchart.domain.Relationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RelationshipRepository extends CrudRepository<Relationship, Long> {

	@Query("MATCH p=()-->(n:Employee) WHERE ID(n) = {nodeId} RETURN p")
	List<Relationship> getParents(@Param("nodeId") Long nodeId);

	@Query("MATCH p=(n:Employee)-->() WHERE ID(n) = {nodeId} RETURN p")
	List<Relationship> getChildren(@Param("nodeId") Long nodeId);

	@Query("MATCH (a:Employee),(b:Employee) WHERE ID(a) = {fromNodeId} AND ID(b) = {toNodeId} CREATE p=(a)-[:MANAGES]->(b) RETURN p")
	Relationship create(@Param("fromNodeId") Long fromNodeId, @Param("toNodeId") Long toNodeId);
}

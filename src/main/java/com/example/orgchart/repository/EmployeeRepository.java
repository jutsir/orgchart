package com.example.orgchart.repository;

import com.example.orgchart.domain.Employee;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	@Query("MATCH (n:Employee) WHERE ({label} IS NULL OR toLower(n.label) CONTAINS toLower({label})) AND " +
			"({canSign} IS NULL OR n.canSign = {canSign}) RETURN n")
	List<Employee> search(@Param("label") String label, @Param("canSign") Boolean canSign);
}

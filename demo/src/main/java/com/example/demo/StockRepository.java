package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.sql.Date;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {
    @Query(value = "SELECT a.close FROM stocks AS a WHERE a.name = ?1 ORDER BY a.date", nativeQuery = true)
    double[] getData(String name);

    @Query(value = "SELECT a.date FROM stocks AS a WHERE a.name = ?1 ORDER BY a.date", nativeQuery = true)
    Date[] getDates(String name);

    @Query(value = "SELECT b.close FROM (SELECT a.date as date, a.close as close FROM stocks AS a WHERE a.name = ?1 ORDER BY a.date DESC LIMIT 257) as b ORDER BY b.date", nativeQuery = true)
    double[] getOneYearData(String name);

    @Query(value = "SELECT b.date FROM (SELECT a.date as date, a.close as close FROM stocks AS a WHERE a.name = ?1 ORDER BY a.date DESC LIMIT 257) as b ORDER BY b.date", nativeQuery = true)
    Date[] getOneYearDates(String name);

    @Query(value = "SELECT a.close FROM stocks AS a WHERE a.name = ?1 AND a.date >= ?2 AND a.date <= ?3 ORDER BY a.date", nativeQuery = true)
    double[] getDataBetween(String name, String start, String end);

    @Query(value = "SELECT a.date FROM stocks AS a WHERE a.name = ?1 AND a.date >= ?2 AND a.date <= ?3 ORDER BY a.date", nativeQuery = true)
    Date[] getDatesBetween(String name, String start, String end);

    @Query(value = "SELECT a.close FROM bonds AS a ORDER BY a.date", nativeQuery = true)
    double[] getBondsData();

    @Query(value = "SELECT a.close FROM bonds AS a WHERE a.date = ?1", nativeQuery = true)
    double getBondsDataByDate(String date);

    @Query(value = "SELECT a.close FROM stocks AS a WHERE a.date = ?2 AND a.name = ?1", nativeQuery = true)
    double getStockDataByDate(String name,String date);
    
    /*
     * THICK CLIENT QUERIES
     */
    
    @Query(value = "SELECT a.close FROM stocks AS a WHERE a.name = ?1 ORDER BY a.date AND a.name", nativeQuery = true)
    double[] getStockData(String name);
    
    @Query(value = "SELECT a.date FROM stocks AS a WHERE a.name = ?1 ORDER BY a.date AND a.name", nativeQuery = true)
    String[] getStockDates(String name);
    
    @Query(value = "SELECT a.close FROM bonds AS a ORDER BY a.date", nativeQuery = true)
    double[] getBondData();
    
    @Query(value = "SELECT a.date FROM bonds AS a ORDER BY a.date", nativeQuery = true)
    String[] getBondDates();

    @Query(value = "SELECT a.date FROM stocks AS a ORDER BY a.date", nativeQuery = true)
	String[] getAllDates();

    @Query(value = "SELECT a.close FROM stocks AS a ORDER BY a.date", nativeQuery = true)
    double[] getAllData();

    @Query(value = "SELECT a.name FROM stocks AS a ORDER BY a.date", nativeQuery = true)
    String[] getAllNames();
    
  
}

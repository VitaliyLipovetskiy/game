package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerRepository extends PagingAndSortingRepository<Player, Long> {

//    long countPlayers();

//    @Lock(LockModeType.NONE)
//    @Query("SELECT COUNT(p.id) FROM Player p")
//    long getPlayerCount();

//    @Query(value = "SELECT * FROM Player p", nativeQuery = true)
//    Collection<Player> fetchAllPlayer();


//    Player addPlayer(Player player);

//    @Query("FROM Player p WHERE p.name = :name")
//    Player findByName(@Param("name") String name);

//    @Query(value = "SELECT AVG(p.age) FROM Player p", nativeQuery = true)
//    int getAverageAge();

//    @Procedure(name = "count_by_name")
//    long getCountByName(@Param("name") String name);

//    @Modifying
//    @Query("UPDATE Player p SET p.name = :name WHERE p.id = :id")
//    void changeName(@Param("id") long id, @Param("name") String name);
}

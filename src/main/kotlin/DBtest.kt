import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class DBtest{

    fun JDBC_TEST(money: Int){

        //JDBC 연결 정보
        val jdbcURL = "jdbc:mysql://localhost:3306/mysql"

        val user = "hitak" //or root(!= hitak)
        val password = "0000"

        //JDBC 드라이버 로드
        Class.forName("com.mysql.cj.jdbc.Driver")

        //데이터베이스 연결
        val connection: Connection = DriverManager.getConnection(jdbcURL, user, password)

        //데이터베이스에 데이터 추가
        insertData(connection, money)

        //데이터베이스에서 데이터 조회
        queryData(connection)

        //연결 닫기
        connection.close()

    }





    private fun insertData(connection: Connection, money: Int) {
        val insertQuery = "INSERT INTO Lotto (money) VALUES (?)"

        //PreparedStatement 사용하여 SQL 쿼리 실행
        val preparedStatement: PreparedStatement = connection.prepareStatement(insertQuery)
        preparedStatement.setInt(1, money)
        preparedStatement.executeUpdate()

        println("Data inserted successfully.")
    }


    private fun queryData(connection: Connection) {
        val selectQuery = "SELECT * FROM Lotto"

        //PreparedStatement 사용하여 SQL 쿼리 실행
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        val resultSet : ResultSet = preparedStatement.executeQuery()

        //결과 출력
        while(resultSet.next()){
            val id = resultSet.getInt("id")
            val money = resultSet.getInt("money")

            println("ID: $id, Money: $money")
        }

        resultSet.close()
        preparedStatement.close()
    }


}
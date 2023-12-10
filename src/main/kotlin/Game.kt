import java.lang.NumberFormatException
import java.sql.Connection
import java.sql.DriverManager
import java.text.NumberFormat
import java.util.Locale
import java.sql.PreparedStatement
import java.sql.ResultSet


class Game{
    var money: Int = 10000 //초기 자금
    val sheet: MutableList<MutableList<Int>> = mutableListOf()

    //JDBC 연결 정보
    val jdbcURL = "jdbc:mysql://localhost:3306/mysql"
    val user = "hitak" //or root(!= hitak)
    val password = "0000"
    lateinit var connection: Connection
    
    fun oneGame() {

        //JDBC 드라이버 로드 Class.forNmae("드라이버_클래스_이름")
        Class.forName("com.mysql.cj.jdbc.Driver")
        //데이터베이스 연결
        connection = DriverManager.getConnection(jdbcURL, user, password)

        while(true) {

            printMoney()
            println("================================================================================================")
            println(" [1:보유중인Sheets확인\t2:로또구매\t3:당첨번호확인\t4:종료\t5:무료충전\t6:DB다루기\t7:무료자동10장]")
            println("================================================================================================")
            print("=> ")
            try {
                val choose = readLine().toString().trim().toInt()
                when (choose) {
                    1 -> {
                        checkSheets()       //현재 sheet 확인(보여주기)
                    }

                    2 -> {
                        purchaseLotto()
                    }

                    3 -> {
                        checkResult()
                    }

                    4 -> {
                        break
                    }

                    5 -> {
                        freeCharge()
                    }

                    6 -> {
                        DB()
                    }

                    7 -> {
                        macro()
                    }
                }
            } catch (e: NumberFormatException){

            }
        }

        //연결 닫기
        connection.close()

    }


    //==================1번 : 보유중인 Sheet 확인 =================
    fun checkSheets(){
        if(sheet.isEmpty())
            println("비어있습니다.")
        else{
            var count = 1
            for(list in sheet){
                print("${count++}번) ")
                for(element in list){
                    print("$element ")
                }
                println()
            }
        }
    }

    //==================2번 : 로또 구매 =================
    fun purchaseLotto(){
        val lotto = Lotto()
        var user: MutableList<Int>
        var choose: Int

        if(money < 1000){
            println("잔액이 부족하여 구매할 수 없습니다.")
            return
        }

        while(true){
            println("[1:자동 \t 2:수동 \t 3:반자동]")
            print("=> ")
            val temp_choose = readLine().toString().trim()
            try {
                choose = temp_choose.toInt()
                break
            } catch (e: NumberFormatException) {
                println("올바른 형식으로 숫자 하나를 입력하세요. (1, 2, 3)")
            }
        }

        while(true){
            when(choose){
                1 -> {
                    println("<자동>")
                    break
                }


                2 -> {
                    println("<수동>")
                    break
                }


                3 -> {
                    println("<반자동>")
                    break
                }

                else -> {
                    choose = (1..3).random()
                }

            }
        }
                //자동 / 수동 / 반자동 선택
                //=====================================================

        var num: Int
        while(true){
            print("매수를 입력하세요.(장당 1,000) => ")
            try{
                num = readLine().toString().trim().toInt()
                if(num <= 0){
                    println("잘못된 입력입니다. 0 과 음수는 입력할 수 없습니다.")
                    continue
                }
                if(num*1000 <= money){
                    money -= num*1000
                    insertMoney(connection, money)
                    println("${num}장 출력됩니다.")
                    when(choose){
                        1 -> {
                            for(i in 1..num){
                                user = lotto.AutoLotto()
                                sheet.add(user)
                                lotto.printLotto(user)
                            }
                            break
                        }
                        2 -> {
                            for(i in 1..num){
                                user = lotto.ManualLotto()
                                sheet.add(user)
                                lotto.printLotto(user)
                            }
                            break
                        }
                        3 -> {
                            for(i in 1..num){
                                user = lotto.HalfAuto()
                                sheet.add(user)
                                lotto.printLotto(user)
                            }
                            break
                        }
                    }
                } else {
                    val d = num*1000 - money
                    val temp = NumberFormat.getNumberInstance(Locale.US).format(d)
                    println("잔액이 부족합니다. 부족한 금액 : ${temp}원")
                }
            } catch (e: NumberFormatException){
                println("올바른 형식으로 숫자 하나를 입력하세요. (1, 2, 3)")
            }
        }




    }

    //==================3번 : 당첨 번호 확인 =================
    fun checkResult(){
        val result = Lotto().winningNumbers()
        println("\t\t<당첨번호>")
        Lotto().printLotto(result.first)
        println("보너스 번호 : ${result.second}")

        println("\t\t<결과>")
        checkLotto(sheet, result)

        sheet.clear()   //이번회차 확인했으니 비우고 다음회차로.
    }

    private fun checkLotto(sheet: MutableList<MutableList<Int>>, prize: Pair<MutableList<Int>, Int>) {
        val result = prize.first
        val bonus = prize.second

        var num = 1

        for(list in sheet){
            var count = 1
            print("${num++}번) ")
            for(element in list){
                print("$element ")
                if(element in result){
                    count++
                }
            }

            if(count == 6){
                val prizeMoney =1952160000
                println("\t\t\t\t1등\t+${prizeMoney}")
                money += prizeMoney
                insertMoney(connection, money)
            } else if(count == 5){
                if(bonus in list){
                    val prizeMoney = 54226666
                    println("\t\t\t\t2등\t+${prizeMoney}")
                    money += prizeMoney
                    insertMoney(connection, money)
                } else {
                    val prizeMoney = 1427017
                    println("\t\t\t\t3등\t+${prizeMoney}")
                    money += prizeMoney
                    insertMoney(connection, money)
                }
            } else if(count == 4){
                val prizeMoney = 50000
                println("\t\t\t\t4등\t+${prizeMoney}")
                money += prizeMoney
                insertMoney(connection, money)
            } else if(count == 3){
                val prizeMoney = 5000
                println("\t\t\t\t5등\t+${prizeMoney}")
                money += prizeMoney
                insertMoney(connection, money)
            } else {
                println("\t\t\t\t꽝")
            }

        }
    }


    //==================4번 : 종료 =================
    //fun exit(){
    //
    //}

    //==================5번 : 무료 충전 =================
    fun freeCharge(){
        val charge = 10000
        money += charge
        insertMoney(connection, money)

        val temp = NumberFormat.getNumberInstance(Locale.US).format(charge)

        println("${temp}원 무료 충전")
    }

    //==================6번 : DB 다루기 =================
    fun DB(){
        DBtest().JDBC_TEST(money)

        // 테이블 구상
        // 플레이어 | 가진돈 | 수익 | 지출 | 무료충전횟수 | 플레이회차(로또회차) | 구매게임수 | 1등당첨횟수 | 2등당첨횟수 | 3등당첨횟수 | 4등당첨횟수 | 5등당첨횟수 | 꽝
        // 이런 데이터들은 상시로 기록하고.
        // 데이터 조회할때 쓰는 함수를 이 DB() 함수로 쓰던지 해야겠는데?
        //일단 이러한 DB는 플레이어의 게임 활동을 추적하기에 적합한 테이블 구조라고 볼 수 있다.
    }

    //================================================
    private fun printMoney(){
        val temp = NumberFormat.getNumberInstance(Locale.US).format(money)
        println("현재 지니고 있는 금액 : ${temp} 원")
    }

    //===============================================
    private fun macro(){
        if(money >= 10000){
            money -= 10000
            insertMoney(connection, money)
            for(i in 1..10){
                sheet.add(Lotto().AutoLotto())
            }
            freeCharge()
        } else {
            println("잔액 부족")
        }
    }





    private fun insertMoney(connection: Connection, money: Int) {
        val insertQuery = "INSERT INTO Lotto (money) VALUES (?)"

        //PreparedStatement 사용하여 SQL 쿼리 실행
        val preparedStatement: PreparedStatement = connection.prepareStatement(insertQuery)
        preparedStatement.setInt(1, money)
        preparedStatement.executeUpdate()

        println("Data inserted successfully.")
    }

    private fun increaseRank(connection: Connection, rank: Int){

    }

    private fun increaseGame(connection: Connection){

    }

    private fun increaseWeek(connection: Connection){

    }
}
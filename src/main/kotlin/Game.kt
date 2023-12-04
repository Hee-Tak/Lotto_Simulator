import java.lang.NumberFormatException
import java.text.NumberFormat
import java.util.Locale

class Game{
    var money: Int = 10000 //초기 자금
    val sheet: MutableList<MutableList<Int>> = mutableListOf()
    
    fun oneGame() {

        while(true){
            println("================================================================================================================")
            println(" [1:보유중인Sheets확인\t2:로또구매\t3:당첨번호확인\t4:종료\t5:무료충전\t6:DB다루기]")
            println("================================================================================================================")
            print("=> ")
            val choose = readLine().toString().trim().toInt()
            when(choose){
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
                    exit()
                }
                5 -> {
                    freeCharge()
                }
                6 -> {
                    DB()
                }
            }
        }
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
        print("매수를 입력하세요.(장당 1000) => ")
        var num: Int
        while(true){
            try{
                num = readLine().toString().trim().toInt()
                if(num*1000 <= money){
                    money -= num*1000
                    println("${num}장 출력됩니다.")
                    when(choose){
                        1 -> {
                            for(i in 1..num){
                                user = lotto.AutoLotto()
                                sheet.add(user)
                                lotto.printLotto(user)
                            }
                        }
                        2 -> {

                        }
                        3 -> {

                        }
                    }
                }
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
            //println("\t\t${count}개 일치")
            if(count == 6){
                val prizeMoney =1952160000
                println("\t\t\t\t1등\t+${prizeMoney}")
                money += prizeMoney
            } else if(count == 5){
                if(bonus in list){
                    val prizeMoney = 54226666
                    println("\t\t\t\t2등\t+${prizeMoney}")
                    money += prizeMoney
                } else {
                    val prizeMoney = 1427017
                    println("\t\t\t\t3등\t+${prizeMoney}")
                    money += prizeMoney
                }
            } else if(count == 4){
                val prizeMoney = 50000
                println("\t\t\t\t4등\t+${prizeMoney}")
                money += prizeMoney
            } else if(count == 3){
                val prizeMoney = 5000
                println("\t\t\t\t5등\t+${prizeMoney}")
                money += prizeMoney
            } else {
                println("\t\t\t\t꽝")
            }

        }
    }


    //==================4번 : 종료 =================
    fun exit(){
        return return
    }

    //==================5번 : 무료 충전 =================
    fun freeCharge(){
        val charge: Int = 5000
        money += charge

        println("${charge}원 무료 충전")
    }

    //==================6번 : DB 다루기 =================
    fun DB(){

    }

}
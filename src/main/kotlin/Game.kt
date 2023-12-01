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
                    checkSheets()
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

    fun purchaseLotto(){

    }

    fun checkResult(){

    }

    fun exit(){

    }

    fun freeCharge(){

    }

    fun DB(){

    }

}
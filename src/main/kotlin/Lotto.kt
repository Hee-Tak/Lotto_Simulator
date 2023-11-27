import java.util.*

class Lotto {
    public fun AutoLotto(): MutableList<Int> {
        val lotto = mutableListOf<Int>()

        while(lotto.size < 6){
            val random = Random().nextInt(45) + 1
            if(!lotto.contains(random))
                lotto.add(random)
        }
        lotto.sort()
        return lotto
    }

    public fun ManualLotto(): MutableList<Int> {
        var lotto = mutableListOf<Int>()

        while(lotto.size < 6){
            print("번호를 입력해 주세요. : ")
            val input: String = readLine().toString()
            val arr = input.split(" ").filter{ it.isNotBlank() && it.matches(Regex("-?\\d+")) }

            arr.map{ it.toInt() }.forEach{
                if(it in 1..45 && it !in lotto && lotto.size < 6)
                    lotto.add(it)
                if(lotto.size >= 6)
                    return@forEach
            }
        }

        lotto.sort()
        return lotto
    }

    public fun HalfAuto(): MutableList<Int> {
        val lotto = mutableListOf<Int>()
        print("1~45의 숫자 1~6개 입력 : ")
        val input: String = readLine().toString()
        val arr = input.split(" ").filter{ it.isNotBlank() && it.matches(Regex("-?\\d+")) }

        arr.map{ it.toInt() }.forEach{

        }
    }

}
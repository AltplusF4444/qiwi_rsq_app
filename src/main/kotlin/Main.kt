import java.util.*
import kotlin.math.sqrt


class Rsa {
    public var N = 0.toLong()
    public var openedExponent = 0.toLong()
    public var closedExponent = 0.toLong()

    public fun CreateKeys(minNum: Long, maxNum: Long) {
        var firstPrimitive = CreatePrimeNumpber(minNum, maxNum)
        var secondPrimitive = CreatePrimeNumpber(minNum, maxNum)
        N = firstPrimitive * secondPrimitive
        var phi = (firstPrimitive - 1) * (secondPrimitive - 1).toLong()
        closedExponent = CreateCoprime(phi)
        openedExponent = CreateReciprocal(phi)
    }

    private fun CreateCoprime(phi: Long): Long {
        var a = 0.toLong()
        var b: Long
        var i = (phi - 2).toLong()
        while (a !== 1.toLong()) {
            a = i
            b = phi
            while (b !== 0.toLong()) {
                a %= b
                var c = a
                a = b
                b = c
            }
            if (a === 1.toLong())
                return i
            i--
        }
        return 0
    }

    private fun CreateReciprocal(phi: Long): Long {
        for (i in 1..phi - 2)

            if (((closedExponent * i) % phi) === 1.toLong())
                return i;
        return 0
    }

    private fun CreatePrimeNumpber(minNum: Long, maxNum: Long): Long {
        var num = (minNum..maxNum).random().toLong()
        var allNumbers: MutableList<Long> = mutableListOf()
        for (i in 0..num) {
            allNumbers.add(i)
        }
        allNumbers[1] = 0

        for (i in 2..num) {
            var j = (2 * i)

            while (j <= num) {
                allNumbers[j.toInt()] = 0

                j += i
            }
        }
        return allNumbers.max()
    }

    public fun GetN() : Long{
        return N
    }

    public fun GetOpenedExponent() : Long{
        return openedExponent
    }

    public fun GetClosedExponent() : Long{
        return closedExponent
    }

    public fun Encryption(messeg : String, openedExponent : Long, N : Long) : String{
        var encryptedMesseg : String = ""

        messeg.forEach {
            var num = funcMod(it.toLong(), openedExponent, N)
            encryptedMesseg +=  num.toString() + 'O'
        }
        encryptedMesseg = encryptedMesseg.substring(0, encryptedMesseg.length - 1)
        return encryptedMesseg
    }

    public fun Decryption(encryptedMesseg : String, closedExponent : Long, N : Long) : String{

        var promString : String = ""
        var decryptedMesseg : String = ""

        var numList : MutableList<Long> = mutableListOf()

        encryptedMesseg.forEach {
            if(it == 'O'){
                numList.add(promString.toLong())
                promString = ""
            }
            else{
                promString += it
            }
        }

        numList.add(promString.toLong())

        numList.forEach {
            decryptedMesseg += funcMod(it, closedExponent, N).toChar()
        }

        return decryptedMesseg
    }

    private fun funcMod(x : Long, y : Long, z : Long) : Long{
        var result = 1.toLong();
        for (i in 0 until y)
            result = (x * result) % z;
        return result;
    }

}


fun main(args: Array<String>) {
    var keys = Rsa()
    var user = Rsa()
    var seller = Rsa()

    keys.CreateKeys(2024, 4048)

    var s = user.Encryption("abcdef", keys.GetOpenedExponent(), keys.GetN())

    println(s)

    s = seller.Decryption(s, keys.GetClosedExponent(), keys.GetN())

    println(s)
}
package com.example.finalproject_logictest_20211226

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.finalproject_logictest_20211226.databinding.ActivityLottoSimulatorBinding
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class LottoSimulatorActivity : BaseActivity() {

    lateinit var binding: ActivityLottoSimulatorBinding

    //    내 당첨번호 목록
    val myNumberList = arrayListOf(6, 15, 23, 30, 35, 42)

    //    랜덤 당첨번호 목록 => 나중에 6개를 채워야함
    val winNumberList = ArrayList<Int>()

    //    당첨번호를 표시할 텍스트뷰 목록 => xml의 텍스트뷰 목록.
    val winNumberTxtList = ArrayList<TextView>()

    //    보너스 번호도 뽑아야함
    var bonusNum = 0

    //    각 등수별 당첨횟수를 기록
    var rankCount1 = 0
    var rankCount2 = 0
    var rankCount3 = 0
    var rankCount4 = 0
    var rankCount5 = 0
    var rankCountNone = 0

    //    소지 금액 / 당첨 금액
    var myMoney = 10000000  // 1천만원 ~ 0원까지
    var earnMoney = 0L  // 0을 대입 : Int다.  10억단위 숫자도 표현하려고 Long으로 대입.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lotto_simulator)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnBuyLotto.setOnClickListener {

//            당첨번호 만들기 기능 실행
            makeWinNubmers()
//            등수 체크 기능 실행
            checkLottoRank()

        }

    }


    fun checkLottoRank() {

//        1천원 소진.
        myMoney -= 1000

//        내 번호를 들고 => 당첨번호를 둘러보면서 => 같은 숫자가 몇개인가? 체크.

        var correctCount = 0  // 맞춘 숫자 기록용 변수

        for (myNum in myNumberList) {

//            내 번호 하나 꺼내면 => 당첨번호 6개를 돌아보자.

            for (winNum in winNumberList) {

//                내 번호와, 당첨번호가 같은가? => 같다면, 맞춘 개수 추가.
                if (myNum == winNum) {
                    correctCount++  // 기존 저장 값에서 1증가.
                }

            }

        }

//        correctCount에 몇개를 맞췄는지 기록되어있다. => 등수 판단에 활용하자.

        if (correctCount == 6) {
            rankCount1++

            earnMoney += 2000000000  // 번 돈을 20억 증가
        } else if (correctCount == 5) {
            rankCount3++

            earnMoney += 1500000  // 번 돈을 50만원 증가
        } else if (correctCount == 4) {
            rankCount3++

            earnMoney += 50000  // 번 돈을 5만원 증가
        } else if (correctCount == 3) {
            rankCount5++

//            돈으로 받지 않는다 => 내 돈 (로또 구매 자금)을 5천원 증가.
//            당첨금액은 늘리지 않는다.

            myMoney += 5000  // 로또 5천원 추가 구매매
        } else {
            rankCountNone++
//            낙첨은 자금 변동이 없다.
        }

//        자금 변동사항도 텍스트뷰에 반영
        binding.txtMyMoney.text =
            "소지금액 : ${NumberFormat.getInstance(Locale.KOREA).format(myMoney)} 원"
        binding.txtEarnMoney.text =
            "당첨금액 : ${NumberFormat.getInstance(Locale.KOREA).format(earnMoney)} 원"


//        새로 변경된 당첨횟수들을 텍스트뷰에 반영
        binding.txtRankCount1.text = "1등 : ${rankCount1}회"
        binding.txtRankCount2.text = "2등 : ${rankCount2}회"
        binding.txtRankCount3.text = "3등 : ${rankCount3}회"
        binding.txtRankCount4.text = "4등 : ${rankCount4}회"
        binding.txtRankCount5.text = "5등 : ${rankCount5}회"
        binding.txtRankCountNone.text = "낙첨 : ${rankCountNone}회"

    }


    fun makeWinNubmers() {

//        당첨 번호 생성 전에, 기존의 당첨번호는 모두 삭제.
        winNumberList.clear()

//        6번 반복 => 당첨번호 6개 생성
        for (i in 0 until 6) {

//            밑의 3가지 프로세스는 => 제대로 된 숫자가 나올때까지 무한 반복.
//            while => if와 문법이 동일한 수준임.

            while (true) {

//            1. 1~45 랜덤값 => Math.random()
//                => 0+1 * 45 <= 랜덤값 * 45 + 1 < 1 * 45

                val randomNum = (Math.random() * 45 + 1).toInt()  // 실수 -> 정수로 변환 : 소수점 자리는 버림 처리

//            2. 나온 랜덤값이 중복인가?
//                당첨번호 목록에, 지금뽑은 랜덤값이 이미 있는가?

                val isDupleOk = !winNumberList.contains(randomNum)

//            3. 중복이 아니면 (써도 된다면) winNumberList에 등록(추가)
//                => 다음 숫자 뽑으러 넘어가자.
//                => while 무한반복 강제종료 => for문에 의해서 다음 숫자를 뽑으러 이동.

                if (isDupleOk) {
                    winNumberList.add(randomNum)

                    break // 제일 가까운 반복문 강제 종료
                }

            }

        }

//        보너스 번호도 추첨해야함.
//        1~45 랜덤, 하나만 추첨.
//        제약 : 기존에 뽑아둔 당첨번호와 중복되면 안됨.

//        무한반복 => 괜찮은 보너스번호가 나오면 추첨 종료.

        while (true) {

            val randomNum = (Math.random() * 45 + 1).toInt()
            val isDupleOk = !winNumberList.contains(randomNum)
            if (isDupleOk) {
                bonusNum = randomNum
                break
            }

        }


//        당첨번호 6개를 => 작은 수 ~ 큰 수 (오름차순) 정렬

        winNumberList.sort()


//       6개의 당첨번호를 로그로 확인 + 텍스트뷰에 반영.
        winNumberList.forEachIndexed { index, winNum ->

            winNumberTxtList[index].text = winNum.toString()

        }

//        보너스 번호도 텍스트뷰에 반영
        binding.txtBonusNum.text = bonusNum.toString()

    }

    override fun setValues() {

//        텍스트뷰 6개를 목록에 추가.
        winNumberTxtList.add(binding.txtWinNum1)
        winNumberTxtList.add(binding.txtWinNum2)
        winNumberTxtList.add(binding.txtWinNum3)
        winNumberTxtList.add(binding.txtWinNum4)
        winNumberTxtList.add(binding.txtWinNum5)
        winNumberTxtList.add(binding.txtWinNum6)


    }
}
package com.example.fragmentstudy

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class CurrencyConverterFragment3 : Fragment() { // 프래그먼트에 주 생성자는 만들지 X
    interface CurrencyCalculationListener {
        fun onCalculate(result:Double,
                        amount: Double,
                        from: String,
                        to: String)
    }

    lateinit var listener : CurrencyCalculationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(activity is CurrencyCalculationListener) {
            listener = activity as CurrencyCalculationListener
        }else {
            throw Exception("CurrencyCalculationListener 미구현")
        }
    }

    val currencyExchangeMap = mapOf(
        "USD" to 1.0,   // Key to Value
        "EUR" to 0.9,
        "JPY" to 110.0,
        "KRW" to 1150.0
    )

    fun calculateCurrency(amount : Double, from: String, to:String) : Double {
        var USDAmount = if(from !="USD") {
            (amount / currencyExchangeMap[from]!!)
        }
        else {
            amount
        }
        return currencyExchangeMap[to]!! * USDAmount
    }

    private lateinit var  fromCurrency : String
    private lateinit var toCurrency : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.currency_converter_fragment3,
            container,
            false
        )
        val calculateBtn = view.findViewById<Button>(R.id.calculate)
        val amount = view.findViewById<EditText>(R.id.amount)
        val exchangeType = view.findViewById<TextView>(R.id.exchange_type)

        fromCurrency = arguments?.getString("from", "USD")!!
        toCurrency = arguments?.getString("to", "USD")!!

        exchangeType.text = "$fromCurrency => $toCurrency 변환"


        calculateBtn.setOnClickListener {
            val result =if(amount.text.toString()=="")
                0.0
            else
                calculateCurrency(
                    amount.text.toString().toDouble(),
                    fromCurrency,
                    toCurrency
                )

            listener.onCalculate(result, amount.text.toString().toDouble(), fromCurrency, toCurrency)
        }

//        amount.addTextChangedListener(object  : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                val result =if(amount.text.toString()=="")
//                    0.toString()
//                else {
//                    calculateCurrency(
//                        amount.text.toString().toDouble(),
//                        fromCurrency,
//                        toCurrency
//                    ).toString()+"원"
//                }
//            }
//            override fun afterTextChanged(p0: Editable?) {}
//        })

            return view
        }

        companion object {
            fun newInstance(from:String, to:String) : CurrencyConverterFragment3 {
                val fragment = CurrencyConverterFragment3()

                // 반응 객체를 만들고 필요한 데이터 저장
                val args = Bundle()
                args.putString("from", from)
                args.putString("to", to)
                fragment.arguments = args

                return fragment
            }
        }
}
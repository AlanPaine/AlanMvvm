package com.alan.networklib.other

import com.alan.networklib.base.BaseData
import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type


/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/08
 * 描述　: 输入T,输出T,并对code统一判断
 */
@Parser(name = "Responses", wrappers = [MutableList::class])
open class ResponseParsers<T> : TypeParser<BaseData<T>> {
    /**
     * 此构造方法适用于任意Class对象，但更多用于带泛型的Class对象，如：List<Student>
     *
     * 用法:
     * Java: .asParser(new ResponseParser<List<Student>>(){})
     * Kotlin: .asParser(object : ResponseParser<List<Student>>() {})
     *
     * 注：此构造方法一定要用protected关键字修饰，否则调用此构造方法将拿不到泛型类型
     */
    protected constructor() : super()

    /**
     * 此构造方法仅适用于不带泛型的Class对象，如: Student.class
     *
     * 用法
     * Java: .asParser(new ResponseParser<>(Student.class))   或者  .asResponse(Student.class)
     * Kotlin: .asParser(ResponseParser(Student::class.java)) 或者  .asResponse<Student>()
     */
    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParse(response: Response): BaseData<T> {
        val data: BaseData<T> = response.convertTo(BaseData::class.java,*types)
        if (data.code != 200) { //code不等于1，说明数据不正确，抛出异常
            throw ParseException(data.code.toString(), data.message, response)
        }
        return data
    }
}
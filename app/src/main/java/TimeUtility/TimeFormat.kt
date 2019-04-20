package TimeUtility

import java.text.SimpleDateFormat

class TimeFormat {

    companion object {
        private val dateTimeParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss-mm:ss")

        // function that receives a timestamp and returns a formatted date
        fun getDate(sdatetime:String):String {
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("dd MMM yyyy")
            return dateFormat.format(dateTime)
        }

        // function that receives a timestamp and returns a formatted time
        fun getTime(sdatetime:String):String {
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("HH:mm")
            return dateFormat.format(dateTime)
        }

        fun getNumericalYear(sdatetime:String):Int {
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("yyyy")
            return dateFormat.format(dateTime).toInt()
        }

        fun getNumericalMonth(sdatetime:String):Int {
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("MM")
            return dateFormat.format(dateTime).toInt()
        }

        fun getNumericalDay(sdatetime:String):Int {
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("dd")
            return dateFormat.format(dateTime).toInt()
        }
    }

}




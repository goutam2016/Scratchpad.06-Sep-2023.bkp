package org.gb.sample.spark.income

import org.apache.commons.csv.CSVRecord
import java.io.IOException
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVFormat

object Converter {
    def convertToPersProfile(lineOfText: String): PersonProfile = {
        var csvRecord = convertToCSVRecord(lineOfText)
        
        if(csvRecord == null) {
            null
        }
        
        val persProfile = new PersonProfile(csvRecord.get(0), csvRecord.get(1), csvRecord.get(2), csvRecord.get(3), csvRecord.get(4), 
                                            csvRecord.get(5), csvRecord.get(6), csvRecord.get(7), csvRecord.get(8), csvRecord.get(9), csvRecord.get(10))
        persProfile
    }
    
    def convertToCSVRecord(lineOfText: String): CSVRecord = {
        var csvRecord: CSVRecord = null

        try {
            val csvRecords = CSVParser.parse(lineOfText, CSVFormat.DEFAULT).getRecords
            csvRecord = csvRecords.get(0)
        } catch {
            case e: IOException => println(s"Could not convert lineOfText: $lineOfText to a CSVRecord.")
        }

        csvRecord
    }
}

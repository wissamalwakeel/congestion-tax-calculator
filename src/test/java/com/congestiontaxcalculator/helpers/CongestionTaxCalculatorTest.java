package com.congestiontaxcalculator.helpers;

import com.congestiontaxcalculator.models.CalculateTaxRequest;
import com.congestiontaxcalculator.models.Car;
import com.congestiontaxcalculator.models.Diplomat;
import com.congestiontaxcalculator.models.Emergency;
import com.congestiontaxcalculator.models.Foreign;
import com.congestiontaxcalculator.models.Interval;
import com.congestiontaxcalculator.models.Military;
import com.congestiontaxcalculator.models.Motorbike;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import javax.ws.rs.BadRequestException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
@Import(com.congestiontaxcalculator.helpers.CongestionTaxCalculator.class)
public class CongestionTaxCalculatorTest {
    private static final Logger LOGGER = LogManager.getLogger(CongestionTaxCalculatorTest.class);

    private Date[] testDatesOneDay;
    private Date[] testDatesMultipleDaysWithWeekendsAndHolidays;

    @Autowired
    CongestionTaxCalculator congestionTaxCalculator;

    @Before
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            testDatesOneDay = objectMapper.readValue(Files.readAllBytes(Paths.get("src/test/resources/request-50.json")), CalculateTaxRequest.class).getDates();
            testDatesMultipleDaysWithWeekendsAndHolidays = objectMapper.readValue(Files.readAllBytes(Paths.get("src/test/resources/request-multiday-89.json")), CalculateTaxRequest.class).getDates();
        } catch (Exception e) {
            throw new BadRequestException("Error parsing the Request body to CalculateTaxRequest");
        }
    }

    private Interval[] getIntervals(String intervalsSourcePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        Interval[] intervals = null;
        try {
             intervals = objectMapper.readValue(new File(intervalsSourcePath), Interval[].class);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Unable to read intervals from resource file for reason: {0}", e.getCause());
        }
        return intervals;
    }

    @Test
    public void getTaxToolFreeVehicleMotorbike() {
        ReflectionTestUtils.setField(congestionTaxCalculator, "intervals", getIntervals("src/main/resources/interval.json"));
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Motorbike(), testDatesOneDay));
    }

    @Test
    public void getTaxToolFreeVehicleMilitary() {
        ReflectionTestUtils.setField(congestionTaxCalculator, "intervals", getIntervals("src/main/resources/interval.json"));
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Military(), testDatesOneDay));
    }

    @Test
    public void getTaxToolFreeVehicleDiplomat() {
        ReflectionTestUtils.setField(congestionTaxCalculator, "intervals", getIntervals("src/main/resources/interval.json"));
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Diplomat(), testDatesOneDay));
    }

    @Test
    public void getTaxToolFreeVehicleEmergency() {
        ReflectionTestUtils.setField(congestionTaxCalculator, "intervals", getIntervals("src/main/resources/interval.json"));
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Emergency(), testDatesOneDay));
    }

    @Test
    public void getTaxToolFreeVehicleForeign() {
        ReflectionTestUtils.setField(congestionTaxCalculator, "intervals", getIntervals("src/main/resources/interval.json"));
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Foreign(), testDatesOneDay));
    }

    @Test
    public void getTaxToolCarOneDay() {
        ReflectionTestUtils.setField(congestionTaxCalculator, "intervals", getIntervals("src/main/resources/interval.json"));
        Assert.assertEquals(50, congestionTaxCalculator.getTax(new Car(), testDatesOneDay));
    }

    @Test
    public void getTaxToolCarMultipleDaysWithWeekendsAndHolidays() {
        ReflectionTestUtils.setField(congestionTaxCalculator, "intervals", getIntervals("src/main/resources/interval.json"));
        Assert.assertEquals(89, congestionTaxCalculator.getTax(new Car(), testDatesMultipleDaysWithWeekendsAndHolidays));
    }
}

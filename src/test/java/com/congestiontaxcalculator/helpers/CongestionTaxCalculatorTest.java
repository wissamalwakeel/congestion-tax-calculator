package com.congestiontaxcalculator.helpers;

import com.congestiontaxcalculator.models.CalculateTaxRequest;
import com.congestiontaxcalculator.models.Car;
import com.congestiontaxcalculator.models.Diplomat;
import com.congestiontaxcalculator.models.Emergency;
import com.congestiontaxcalculator.models.Foreign;
import com.congestiontaxcalculator.models.Military;
import com.congestiontaxcalculator.models.Motorbike;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import javax.ws.rs.BadRequestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CongestionTaxCalculatorTest {

    private Date[] testDatesOneDay;
    private Date[] testDatesMultipleDaysWithWeekendsAndHolidays;

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

    @Test
    public void getTaxToolFreeVehicleMotorbike() {
        CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Motorbike(), testDatesOneDay));
    }

    @Test
    public void getTaxToolFreeVehicleMilitary() {
        CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Military(), testDatesOneDay));
    }

    @Test
    public void getTaxToolFreeVehicleDiplomat() {
        CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Diplomat(), testDatesOneDay));
    }

    @Test
    public void getTaxToolFreeVehicleEmergency() {
        CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Emergency(), testDatesOneDay));
    }

    @Test
    public void getTaxToolFreeVehicleForeign() {
        CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();
        Assert.assertEquals(0, congestionTaxCalculator.getTax(new Foreign(), testDatesOneDay));
    }

    @Test
    public void getTaxToolCarOneDay() {
        CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();
        Assert.assertEquals(50, congestionTaxCalculator.getTax(new Car(), testDatesOneDay));
    }

    @Test
    public void getTaxToolCarMultipleDaysWithWeekendsAndHolidays() {
        CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();
        Assert.assertEquals(50, congestionTaxCalculator.getTax(new Car(), testDatesMultipleDaysWithWeekendsAndHolidays));
    }
}

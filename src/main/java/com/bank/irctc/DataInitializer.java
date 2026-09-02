package com.bank.irctc;

import com.bank.irctc.entity.Station;
import com.bank.irctc.entity.Train;
import com.bank.irctc.entity.TrainSchedule;
import com.bank.irctc.repository.StationRepository;
import com.bank.irctc.repository.TrainRepository;
import com.bank.irctc.repository.TrainScheduleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final TrainScheduleRepository scheduleRepository;

    public DataInitializer(TrainRepository trainRepository, 
                           StationRepository stationRepository,
                           TrainScheduleRepository scheduleRepository) {
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Add Stations (Comprehensive List across India)
        if (stationRepository.count() == 0) {
            stationRepository.saveAll(Arrays.asList(
                // Delhi
                new Station("NDLS", "New Delhi", "New Delhi", "Delhi"),
                new Station("DLI", "Old Delhi Jn", "Delhi", "Delhi"),
                new Station("NZM", "Hazrat Nizamuddin", "Delhi", "Delhi"),
                new Station("ANVT", "Anand Vihar Terminal", "Delhi", "Delhi"),
                
                // Maharashtra
                new Station("MMCT", "Mumbai Central", "Mumbai", "Maharashtra"),
                new Station("CSMT", "Chhatrapati Shivaji Maharaj Terminus", "Mumbai", "Maharashtra"),
                new Station("BDTS", "Bandra Terminus", "Mumbai", "Maharashtra"),
                new Station("PUNE", "Pune Jn", "Pune", "Maharashtra"),
                new Station("NGP", "Nagpur Jn", "Nagpur", "Maharashtra"),
                new Station("SUR", "Solapur Jn", "Solapur", "Maharashtra"),
                
                // Uttar Pradesh
                new Station("LKO", "Lucknow NR", "Lucknow", "UP"),
                new Station("LJN", "Lucknow Jn (NER)", "Lucknow", "UP"),
                new Station("CNB", "Kanpur Central", "Kanpur", "UP"),
                new Station("BSB", "Varanasi Jn", "Varanasi", "UP"),
                new Station("MGS", "Pt. Deen Dayal Upadhyaya Jn", "Mughalsarai", "UP"),
                new Station("AGC", "Agra Cantt", "Agra", "UP"),
                new Station("BE", "Bareilly Jn", "Bareilly", "UP"),
                new Station("PRYJ", "Prayagraj Jn", "Prayagraj", "UP"),
                new Station("GKP", "Gorakhpur Jn", "Gorakhpur", "UP"),
                new Station("GZB", "Ghaziabad Jn", "Ghaziabad", "UP"),
                new Station("MTJ", "Mathura Jn", "Mathura", "UP"),
                
                // West Bengal
                new Station("HWH", "Howrah Jn", "Kolkata", "West Bengal"),
                new Station("SDAH", "Sealdah", "Kolkata", "West Bengal"),
                new Station("KOAA", "Kolkata", "Kolkata", "West Bengal"),
                new Station("NJP", "New Jalpaiguri Jn", "Siliguri", "West Bengal"),
                
                // Tamil Nadu
                new Station("MAS", "MGR Chennai Central", "Chennai", "Tamil Nadu"),
                new Station("MS", "Chennai Egmore", "Chennai", "Tamil Nadu"),
                new Station("MDU", "Madurai Jn", "Madurai", "Tamil Nadu"),
                new Station("CBE", "Coimbatore Jn", "Coimbatore", "Tamil Nadu"),
                
                // Karnataka
                new Station("SBC", "KSR Bengaluru City Jn", "Bengaluru", "Karnataka"),
                new Station("YPR", "Yesvantpur Jn", "Bengaluru", "Karnataka"),
                new Station("MYS", "Mysuru Jn", "Mysuru", "Karnataka"),
                new Station("UBL", "SSS Hubballi Jn", "Hubli", "Karnataka"),
                
                // Rajasthan
                new Station("JP", "Jaipur Jn", "Jaipur", "Rajasthan"),
                new Station("JU", "Jodhpur Jn", "Jodhpur", "Rajasthan"),
                new Station("AII", "Ajmer Jn", "Ajmer", "Rajasthan"),
                new Station("BKN", "Bikaner Jn", "Bikaner", "Rajasthan"),
                
                // Gujarat
                new Station("ADI", "Ahmedabad Jn", "Ahmedabad", "Gujarat"),
                new Station("ST", "Surat", "Surat", "Gujarat"),
                new Station("BRC", "Vadodara Jn", "Vadodara", "Gujarat"),
                new Station("RJT", "Rajkot Jn", "Rajkot", "Gujarat"),
                
                // Bihar
                new Station("PNBE", "Patna Jn", "Patna", "Bihar"),
                new Station("GAYA", "Gaya Jn", "Gaya", "Bihar"),
                new Station("DBG", "Darbhanga Jn", "Darbhanga", "Bihar"),
                new Station("MFP", "Muzaffarpur Jn", "Muzaffarpur", "Bihar"),
                
                // Madhya Pradesh
                new Station("BPL", "Bhopal Jn", "Bhopal", "MP"),
                new Station("RKMP", "Rani Kamalapati (Habibganj)", "Bhopal", "MP"),
                new Station("INDB", "Indore Jn", "Indore", "MP"),
                new Station("GWL", "Gwalior Jn", "Gwalior", "MP"),
                new Station("JBP", "Jabalpur Jn", "Jabalpur", "MP"),
                
                // Telangana & AP
                new Station("SC", "Secunderabad Jn", "Hyderabad", "Telangana"),
                new Station("HYB", "Hyderabad Deccan", "Hyderabad", "Telangana"),
                new Station("BZA", "Vijayawada Jn", "Vijayawada", "Andhra Pradesh"),
                new Station("VSKP", "Visakhapatnam Jn", "Visakhapatnam", "Andhra Pradesh"),
                
                // Kerala
                new Station("TVC", "Thiruvananthapuram Central", "Thiruvananthapuram", "Kerala"),
                new Station("ERS", "Ernakulam Jn (South)", "Kochi", "Kerala"),
                
                // Other Major Junctions
                new Station("ITR", "Itwari Jn", "Nagpur", "Maharashtra"),
                new Station("BSP", "Bilaspur Jn", "Bilaspur", "Chhattisgarh"),
                new Station("TATA", "Tatanagar Jn", "Jamshedpur", "Jharkhand"),
                new Station("BBSR", "Bhubaneswar", "Bhubaneswar", "Odisha")
            ));
        }

        // 2. Add More Trains to connect these new stations
        if (trainRepository.count() == 0) {
            // Rajdhanis
            Train t1 = createTrain("12435", "Rajdhani Express", "New Delhi", "Mumbai Central", "16:25", "08:15", 300, 500.0, 1500.0, 2500.0, 3500.0);
            Train t2 = createTrain("12423", "Dibrugarh Rajdhani", "New Delhi", "New Jalpaiguri Jn", "16:10", "13:15", 300, 600.0, 1800.0, 2800.0, 4000.0);
            Train t3 = createTrain("12952", "Mumbai Rajdhani", "Mumbai Central", "New Delhi", "17:00", "08:35", 350, 500.0, 1500.0, 2500.0, 3500.0);
            
            // Shatabdis
            Train t4 = createTrain("12002", "Bhopal Shatabdi", "New Delhi", "Bhopal Jn", "06:00", "14:10", 400, 0.0, 1200.0, 1800.0, 0.0);
            Train t5 = createTrain("12012", "Kalka Shatabdi", "New Delhi", "Chandigarh", "17:15", "20:30", 450, 0.0, 800.0, 1200.0, 0.0);
            
            // Mail/Express
            Train t6 = createTrain("12230", "Lucknow Mail", "New Delhi", "Lucknow NR", "22:00", "06:45", 500, 350.0, 950.0, 1400.0, 2200.0);
            Train t7 = createTrain("14316", "Intercity Express", "New Delhi", "Bareilly Jn", "16:35", "21:50", 600, 180.0, 500.0, 0.0, 0.0);
            Train t8 = createTrain("12301", "Kolkata Rajdhani", "Howrah Jn", "New Delhi", "16:50", "10:00", 300, 600.0, 1800.0, 3000.0, 4500.0);
            Train t9 = createTrain("12622", "Tamil Nadu Express", "New Delhi", "MGR Chennai Central", "21:05", "06:15", 800, 750.0, 1800.0, 2600.0, 0.0);
            Train t10 = createTrain("12724", "Telangana Express", "New Delhi", "Hyderabad Deccan", "17:25", "19:00", 700, 700.0, 1750.0, 2500.0, 0.0);

            // Additional Major Trains across India
            Train t11 = createTrain("12138", "Punjab Mail", "Firozpur Cantt", "Mumbai CSMT", "21:40", "07:35", 600, 450.0, 1200.0, 1800.0, 3000.0);
            Train t12 = createTrain("12802", "Purushottam Express", "New Delhi", "Puri", "22:40", "05:20", 500, 550.0, 1500.0, 2200.0, 0.0);
            Train t13 = createTrain("12904", "Golden Temple Mail", "Amritsar", "Mumbai Central", "18:55", "05:05", 500, 500.0, 1400.0, 2100.0, 3200.0);
            Train t14 = createTrain("12102", "Jnaneswari Express", "Howrah Jn", "Pune Jn", "22:50", "09:05", 400, 0.0, 1800.0, 2600.0, 3800.0);
            Train t15 = createTrain("12616", "Grand Trunk Express", "New Delhi", "MGR Chennai Central", "16:10", "04:30", 700, 750.0, 1800.0, 2600.0, 0.0);
            Train t16 = createTrain("12214", "Duronto Express", "Delhi Sarai Rohilla", "Yesvantpur Jn", "22:10", "07:50", 300, 0.0, 2800.0, 3800.0, 5200.0);
            Train t17 = createTrain("12259", "Duronto Express", "Sealdah", "Bikaner Jn", "18:30", "15:45", 300, 700.0, 1900.0, 2800.0, 4200.0);
            Train t18 = createTrain("12565", "Bihar Sampark Kranti", "Darbhanga Jn", "New Delhi", "08:25", "05:05", 600, 450.0, 1200.0, 1800.0, 0.0);
            Train t19 = createTrain("12142", "Patliputra Express", "Patliputra Jn", "Mumbai LTT", "11:05", "14:10", 600, 500.0, 1400.0, 2000.0, 0.0);
            Train t20 = createTrain("12397", "Mahabodhi Express", "Gaya Jn", "New Delhi", "14:00", "04:10", 600, 420.0, 1150.0, 1700.0, 0.0);
            Train t21 = createTrain("12137", "Punjab Mail", "Mumbai CSMT", "Firozpur Cantt", "19:35", "05:10", 600, 450.0, 1200.0, 1800.0, 3000.0);
            Train t22 = createTrain("12472", "Swaraj Express", "Shri Mata Vaishno Devi Katra", "Bandra Terminus", "11:15", "18:05", 500, 600.0, 1600.0, 2400.0, 0.0);
            Train t23 = createTrain("12860", "Gitanjali Express", "Howrah Jn", "Mumbai CSMT", "14:05", "21:20", 500, 650.0, 1700.0, 2500.0, 0.0);
            Train t24 = createTrain("12101", "Jnaneswari Express", "Pune Jn", "Howrah Jn", "09:00", "18:50", 400, 0.0, 1800.0, 2600.0, 3800.0);
            Train t25 = createTrain("12615", "Grand Trunk Express", "MGR Chennai Central", "New Delhi", "18:50", "06:30", 700, 750.0, 1800.0, 2600.0, 0.0);
            Train t26 = createTrain("12213", "Duronto Express", "Yesvantpur Jn", "Delhi Sarai Rohilla", "23:40", "07:30", 300, 0.0, 2800.0, 3800.0, 5200.0);
            Train t27 = createTrain("22436", "Vande Bharat Express", "New Delhi", "Varanasi Jn", "06:00", "14:00", 200, 0.0, 1200.0, 1800.0, 2500.0);
            Train t28 = createTrain("22435", "Vande Bharat Express", "Varanasi Jn", "New Delhi", "15:00", "23:00", 200, 0.0, 1200.0, 1800.0, 2500.0);
            Train t29 = createTrain("20901", "Vande Bharat Express", "Mumbai Central", "Gandhinagar Cap", "06:10", "12:25", 200, 0.0, 1400.0, 2100.0, 2800.0);
            Train t30 = createTrain("20902", "Vande Bharat Express", "Gandhinagar Cap", "Mumbai Central", "14:05", "20:25", 200, 0.0, 1400.0, 2100.0, 2800.0);

            // Even More Realistic Indian Trains
            Train t31 = createTrain("12302", "Kolkata Rajdhani", "New Delhi", "Howrah Jn", "16:55", "09:50", 300, 600.0, 1800.0, 3000.0, 4500.0);
            Train t32 = createTrain("12229", "Lucknow Mail", "Lucknow NR", "New Delhi", "22:00", "06:45", 500, 350.0, 950.0, 1400.0, 2200.0);
            Train t33 = createTrain("12951", "Mumbai Rajdhani", "Mumbai Central", "New Delhi", "17:00", "08:35", 350, 500.0, 1500.0, 2500.0, 3500.0);
            Train t34 = createTrain("12001", "Bhopal Shatabdi", "Bhopal Jn", "New Delhi", "15:10", "23:50", 400, 0.0, 1200.0, 1800.0, 0.0);
            Train t35 = createTrain("12621", "Tamil Nadu Express", "MGR Chennai Central", "New Delhi", "22:00", "07:05", 800, 750.0, 1800.0, 2600.0, 0.0);
            Train t36 = createTrain("12723", "Telangana Express", "Hyderabad Deccan", "New Delhi", "06:25", "09:05", 700, 700.0, 1750.0, 2500.0, 0.0);
            Train t37 = createTrain("12424", "Dibrugarh Rajdhani", "New Jalpaiguri Jn", "New Delhi", "13:10", "10:15", 300, 600.0, 1800.0, 2800.0, 4000.0);
            Train t38 = createTrain("12801", "Purushottam Express", "Puri", "New Delhi", "21:45", "04:20", 500, 550.0, 1500.0, 2200.0, 0.0);
            Train t39 = createTrain("12903", "Golden Temple Mail", "Mumbai Central", "Amritsar", "18:45", "05:20", 500, 500.0, 1400.0, 2100.0, 3200.0);
            Train t40 = createTrain("22436", "Vande Bharat", "New Delhi", "Varanasi", "06:00", "14:00", 200, 0.0, 1200.0, 1800.0, 0.0);
            Train t41 = createTrain("12011", "Kalka Shatabdi", "Chandigarh", "New Delhi", "06:50", "10:15", 450, 0.0, 800.0, 1200.0, 0.0);
            Train t42 = createTrain("12260", "Duronto Express", "Bikaner Jn", "Sealdah", "12:15", "09:40", 300, 700.0, 1900.0, 2800.0, 4200.0);
            Train t43 = createTrain("12141", "Patliputra Express", "Mumbai LTT", "Patliputra Jn", "23:35", "03:50", 600, 500.0, 1400.0, 2000.0, 0.0);
            Train t44 = createTrain("12398", "Mahabodhi Express", "New Delhi", "Gaya Jn", "12:50", "02:55", 600, 420.0, 1150.0, 1700.0, 0.0);
            Train t45 = createTrain("12566", "Bihar Sampark Kranti", "New Delhi", "Darbhanga Jn", "13:00", "09:30", 600, 450.0, 1200.0, 1800.0, 0.0);
            Train t46 = createTrain("12471", "Swaraj Express", "Bandra Terminus", "Shri Mata Vaishno Devi Katra", "11:00", "17:25", 500, 600.0, 1600.0, 2400.0, 0.0);
            Train t47 = createTrain("12859", "Gitanjali Express", "Mumbai CSMT", "Howrah Jn", "06:00", "12:30", 500, 650.0, 1700.0, 2500.0, 0.0);
            Train t48 = createTrain("22415", "Vande Bharat", "Varanasi", "New Delhi", "15:00", "23:00", 200, 0.0, 1200.0, 1800.0, 0.0);
            Train t49 = createTrain("12611", "Chennai Garib Rath", "MGR Chennai Central", "Hazrat Nizamuddin", "06:10", "10:20", 800, 0.0, 1100.0, 0.0, 0.0);
            Train t50 = createTrain("12612", "Nizamuddin Garib Rath", "Hazrat Nizamuddin", "MGR Chennai Central", "15:55", "20:15", 800, 0.0, 1100.0, 0.0, 0.0);

            List<Train> savedTrains = trainRepository.saveAll(Arrays.asList(
                t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, 
                t11, t12, t13, t14, t15, t16, t17, t18, t19, t20,
                t21, t22, t23, t24, t25, t26, t27, t28, t29, t30,
                t31, t32, t33, t34, t35, t36, t37, t38, t39, t40,
                t41, t42, t43, t44, t45, t46, t47, t48, t49, t50
            ));

            // 3. Create Schedules for the next 15 days for all trains
            if (scheduleRepository.count() == 0) {
                LocalDate today = LocalDate.now();
                for (int i = 0; i < 15; i++) {
                    LocalDate date = today.plusDays(i);
                    for (Train t : savedTrains) {
                        TrainSchedule s = new TrainSchedule();
                        s.setTrain(t);
                        s.setJourneyDate(date);
                        s.setAvailableSeats(t.getTotalSeats() - (i * 5));
                        s.setTotalSeats(t.getTotalSeats());
                        s.setStatus("ACTIVE");
                        scheduleRepository.save(s);
                    }
                }
            }
            System.out.println("Enhanced Realistic Data Initialized: 60+ Stations, 10+ Major Trains, and 15 days of Schedules.");
        }
    }

    private Train createTrain(String no, String name, String src, String dest, String dep, String arr, int seats, double sl, double a3, double a2, double a1) {
        Train t = new Train();
        t.setTrainNumber(no); t.setTrainName(name);
        t.setSource(src); t.setDestination(dest);
        t.setDepartureTime(dep); t.setArrivalTime(arr);
        t.setTotalSeats(seats); t.setAvailableSeats(seats);
        t.setSleeperFare(sl > 0 ? sl : null);
        t.setAc3Fare(a3 > 0 ? a3 : null);
        t.setAc2Fare(a2 > 0 ? a2 : null);
        t.setAc1Fare(a1 > 0 ? a1 : null);
        return t;
    }
}

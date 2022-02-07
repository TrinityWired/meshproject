import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.FileReader;

class Trinity {
  public static void main(String[] args) throws FileNotFoundException {


    Scanner sc= new Scanner(System.in);    //System.in is a standard input stream  
System.out.print("Enter year - ");  
int year= sc.nextInt();  
System.out.print("Enter Month- ");  
int Month= sc.nextInt();  
System.out.print("Enter day- ");  
int day= sc.nextInt(); 
System.out.print("Enter hour- ");
int hour= sc.nextInt();
System.out.println("Enter minute- ");
int minute= sc.nextInt();

    LocalDate d = LocalDate.of(year, Month, day);
    LocalDateTime datetime = d.atTime(hour, minute, 9);

    List<String> result = find_available_service("service_hours.csv", datetime);
    result.stream().forEach(System.out::println);
    result = find_available_service_by_categories("service_hours.csv", datetime, "Edgars Eggs");
    System.out.println("second function");
    result.stream().forEach(System.out::println);

  }

  public static List<String> find_available_service_by_categories(String csv_filename, LocalDateTime datetime,
      String selected_categories) throws FileNotFoundException {
    List<String> result = find_available_service(csv_filename, datetime);

    List<String> updatedResult = result.stream().filter(x -> x.contains(selected_categories))
        .collect(Collectors.toList());
    return updatedResult;
  }

  public static List<String> find_available_service(String csv_filename, LocalDateTime d) throws FileNotFoundException {
    List<String> company_list = new ArrayList<String>();
    String dateTimevalue = d.toString();
    String[] dateTime = dateTimevalue.split("T");
    LocalDate date = LocalDate.parse(dateTime[0]);
    int hour = Integer.parseInt(dateTime[1].split(":")[0]);
    int minute = Integer.parseInt(dateTime[1].split(":")[1]);
    String meridian = "AM";
    String day = date.getDayOfWeek().toString().substring(0, 3).toLowerCase();

    if (hour > 12) {
      hour = hour - 12;
      meridian = "PM";
    }
    // System.out.println(day);
    // System.out.println(hour);
    // System.out.println(minute);
    // System.out.println(meridian);
    String line = "";
    String splitBy = "\"";
    try {
      String[] daysOfWeek = { "mon", "tue", "wed", "thu", "fri", "sat", "sun" };
      // parsing a CSV file into BufferedReader class constructor
      BufferedReader br = new BufferedReader(new FileReader(csv_filename));

      while ((line = br.readLine()) != null) // returns a Boolean value
      {
        String[] employee = line.split(splitBy); // use comma as separator
        for (int i = 0; i < employee.length; i++) {
          if (i == 5 || i % 5 == 0) {
            String[] timePeriod = employee[i].split("/");
            for (String string : timePeriod) {

              String to = "";
              String timefrom = "";
              String timeTo = "";
              String fromTo = string.split(";")[0].toLowerCase(); // week day from(mon) and to(sun)
              String from = fromTo.split("-")[0];

              if (fromTo.split("-").length > 1) {
                to = fromTo.split("-")[1];
              }

              if (string.split(";").length > 2) {
                timefrom = string.split(";")[1];
                timeTo = string.split(";")[2];
              }
              int startIndex = Arrays.asList(daysOfWeek).indexOf(from);
              int endIndex = Arrays.asList(daysOfWeek).indexOf(to);

              int givenDayIndex = Arrays.asList(daysOfWeek).indexOf(day);

              if (givenDayIndex >= startIndex && givenDayIndex <= endIndex) {

                if (!timefrom.equals("")) {

                  int startHour = 0;
                  if (timefrom.split(":")[0].trim().length() > 3) {
                    startHour = Integer.parseInt(timefrom.split(":")[0].trim().substring(0, 2));
                  } else {
                    startHour = Integer.parseInt(timefrom.split(":")[0].trim().substring(0, 1));
                  }

                  int startMinute = 0;
                  if (timefrom.split(":").length > 1) {
                    if (timefrom.split(":").length > 1) {
                      startMinute = Integer.parseInt(timefrom.split(":")[1].trim().substring(0, 2));
                    } else {
                      startMinute = Integer.parseInt(timefrom.split(":")[0]);
                    }
                  }

                  int endHour = 0;

                  if (timeTo.split(":")[0].trim().length() > 3) {
                    endHour = Integer.parseInt(timeTo.split(":")[0].trim().substring(0, 2));
                  } else {
                    endHour = Integer.parseInt(timeTo.split(":")[0].trim().substring(0, 1));
                  }

                  int endMinute = 0;
                  if (timeTo.split(":").length > 1) {
                    endMinute = Integer.parseInt(timeTo.split(":")[1].substring(0, 2));
                  }

                  if (hour >= startHour && hour <= endHour) {

                    if (hour == startHour) {
                      if (minute >= startMinute) {

                        List<String> res = Arrays.stream(employee).filter(x -> !x.equals(","))
                            .collect(Collectors.toList());
                        // Arrays.stream(employee).forEach(System.out::print);

                        company_list.addAll(res);

                      }
                    } else if (hour == endHour) {
                      if (minute <= endMinute) {

                        List<String> res = Arrays.stream(employee).filter(x -> !x.equals(","))
                            .collect(Collectors.toList());
                        // Arrays.stream(employee).forEach(System.out::print);

                        company_list.addAll(res);

                      }
                    } else {

                      List<String> res = Arrays.stream(employee).filter(x -> !x.equals(","))
                          .collect(Collectors.toList());

                      // Arrays.stream(employee).forEach(System.out::print);
                      company_list.addAll(res);

                    }
                  }
                }

              }

            }
          }

        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return company_list;
  }

}
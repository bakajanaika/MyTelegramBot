package com.example.mytgbot.services;

import com.example.mytgbot.cfgs.BotConfig;
import com.example.mytgbot.models.FromMessage;
import com.example.mytgbot.models.PollResults;
import com.example.mytgbot.models.WeatherData;
import com.example.mytgbot.repos.FromMessageRepo;
import com.example.mytgbot.repos.PollResRepo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final WeatherService weatherService;
    private Map<Long, String> chatStates = new HashMap<>();
    final BotConfig config;
    final FromMessageRepo fromMessageRepo;
    final PollResRepo pollResRepo;

    public TelegramBot(WeatherService weatherService, BotConfig config, FromMessageRepo fromMessageRepo, PollResRepo pollResRepo){
        this.weatherService = weatherService;
        this.config=config;
        this.fromMessageRepo = fromMessageRepo;
        this.pollResRepo = pollResRepo;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    public void sendPollTo() {
        SendPoll sendPoll = new SendPoll();
        sendPoll.setChatId("@zxczxcasd123");
        sendPoll.setQuestion("???");
        sendPoll.setOptions(List.of("aaa", "bbb"));
        sendPoll.setIsAnonymous(false);
        try {
            Message sentPoll = execute(sendPoll);
            long pollId = Long.parseLong(sentPoll.getPoll().getId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText() || update.hasPollAnswer()){
                try {

                    PollAnswer pollAnswer = update.getPollAnswer();
                    String pollId = pollAnswer.getPollId();
                    List<Integer> optionId = pollAnswer.getOptionIds();
                    PollResults pollResults = new PollResults();
                    pollResults.setUsername(pollAnswer.getUser().getUserName());
                    pollResults.setPollId(pollId);
                    pollResults.setSelectedOption(pollAnswer.getOptionIds().toString());
                    pollResRepo.save(pollResults);

                    System.out.println("aaaaaa");
                }catch (RuntimeException e){
                    e.printStackTrace();
                }

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            FromMessage fromMessage = new FromMessage();
            fromMessage.setChatId(update.getMessage().getChat().getUserName());
            fromMessage.setText(messageText);
            fromMessage.setDateTime(LocalDateTime.now());
            fromMessageRepo.save(fromMessage);

            if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                sendPollTo();
            }



            else if(messageText.equals("/weather")){
                chatStates.put(chatId, "waiting_for_city");
                sendMessage(chatId, "Введите название города: ");

            }
            else if (chatStates.get(chatId) != null && chatStates.get(chatId).equals("waiting_for_city")) {
                String cityName = messageText;
                String sourceLanguage = "ru";
                String targetLanguage = "en";
                try{

                String url = "https://translate.googleapis.com/translate_a/single" +
                        "?client=gtx" +
                        "&sl=" + sourceLanguage +
                        "&tl=" + targetLanguage +
                        "&dt=t&q=" + URLEncoder.encode(cityName, StandardCharsets.UTF_8);


                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder responsed = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responsed.append(inputLine);
                    }
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                chatStates.remove(chatId);
                try {
                    currentWeather(chatId, cityName);
                }catch (HttpClientErrorException e){
                    sendMessage(chatId, e.getMessage());
                }

            }
            else if (messageText.equals("/cat")){
                getCatCommand(chatId);
            }
            else if (messageText.equals("/space")){
                getExplanationCosmo(chatId);
                getCosmoPic(chatId);
            }
            
        }
    }

    private void startCommandReceived(long chatId, String name){
        String answer = "Salam, " + name + ", " + "kandaisin?";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String sendText){
        SendMessage message = new SendMessage();
        message.setChatId(valueOf(chatId));
        message.setText(sendText);

        try{
            execute(message);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);

        }
    }

    public void currentWeather(long chatId, String name){

        String apiUrl =
                "https://api.weatherapi.com/v1/current.json?key=7410e722f44e4c02bd353418230410&q="+name+"&lang=ru";
        try {
            WeatherData weatherData = weatherService.fetchWeatherData(apiUrl);

            String location = weatherData.getLocation().getName() + ", "
                    + weatherData.getLocation().getCountry() + " ";
            double temperature = weatherData.getCurrent().getTemp_c();
            double feelsTemperature = weatherData.getCurrent().getFeelslike_c();
            String condition = weatherData.getCurrent().getCondition().getText();
            String message = "Текущая погода в " +location+ ": \n"+temperature+"°C "+ " ощущается как " + feelsTemperature +"°C"+ "\nСостояние погоды: " +condition;
            sendMessage(chatId,message);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getCosmoPic(long chatId){
        try{
            ApodService apodService = new ApodService();
            String apodUrl = apodService.getCosmo();
            InputFile cosmoImage = new InputFile(apodUrl);
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(valueOf(chatId));
            sendPhoto.setPhoto(cosmoImage);
            execute(sendPhoto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getCatCommand(long chatId) {
        try {
            CatService catService = new CatService();
            String catImageUrl = catService.getCat();

            InputFile catImage = new InputFile(catImageUrl);
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(valueOf(chatId));
            sendPhoto.setPhoto(catImage);
            execute(sendPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getExplanationCosmo(long chatId){
        try{
            ApodService apodService = new ApodService();
            String response = apodService.getCosmoExplanation();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(valueOf(chatId));
            sendMessage.setText(response);
            execute(sendMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

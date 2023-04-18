package com.example.news_restapi.model;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    final String JDBC_URL = "jdbc:mysql://localhost:3306/news?serverTimezone=Asia/Seoul";
    final String JDBC_USER = "root";
    final String JDBC_PASSWORD = "31133";

    public Connection open() {
        /*  DB 연결을 가져오는 메서드.
            각각의 메서드마다 연결을 만들고 해제하는 구조.
         */
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void addNews(News news) throws Exception {
        Connection connection = open();

        String sql = "INSERT INTO `news` (`title`, `img`, `date`, `content`) VALUE (?, ?, now(), ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        try(connection; preparedStatement) {
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getImg());
            preparedStatement.setString(3, news.getContent());
            preparedStatement.executeUpdate();
        }
    }

    public List<News> getAll() throws Exception {
        Connection connection = open();
        List<News> newsList = new ArrayList<>();

        String sql = "SELECT `aid`, `title`, `date` FROM `news`";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        try(connection; preparedStatement; resultSet) {
            while (resultSet.next()) {
                News news = new News();
                news.setAid(resultSet.getInt("aid"));
                news.setTitle(resultSet.getString("title"));
                news.setDate(resultSet.getString("date"));
                newsList.add(news);
            }
        }
        return newsList;
    }

    public News getNews(int aid) throws Exception {
        Connection connection = open();

        News news = new News();
        String sql = "SELECT `aid`, `title`, `img`, `date`, `content` FROM `news` WHERE `aid` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, aid);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        try(connection; preparedStatement; resultSet) {
            news.setAid(resultSet.getInt("aid"));
            news.setTitle(resultSet.getString("title"));
            news.setImg(resultSet.getString("img"));
            news.setDate(resultSet.getString("date"));
            news.setContent(resultSet.getString("content"));
            return news;
        }
    }

    public void delNews(int aid) throws Exception {
        Connection connection = open();
        String sql = "DELETE FROM `news` WHERE `aid` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        try(connection; preparedStatement) {
            preparedStatement.setInt(1, aid);
            if (preparedStatement.executeUpdate() == 0) { // 삭제된 기사가 없을 경우
                throw new SQLException("DB에러");

            }
        }

    }

}
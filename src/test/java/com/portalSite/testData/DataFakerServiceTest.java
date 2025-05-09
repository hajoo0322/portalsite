package com.portalSite.testData;

import com.portalSite.auth.RegisterRequest;
import com.portalSite.blog.entity.Blog;
import com.portalSite.blog.entity.BlogBoard;
import com.portalSite.blog.repository.BlogBoardRepository;
import com.portalSite.blog.repository.BlogRepository;
import com.portalSite.cafe.entity.*;
import com.portalSite.cafe.repository.*;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import com.portalSite.news.entity.NewsCategory;
import com.portalSite.news.repository.NewsCategoryRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Locale;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class DataFakerServiceTest {

    @Autowired
    CafePostRepository cafePostRepository;
    @Autowired
    CafeMemberRepository cafeMemberRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DataSource dataSource;
    @Autowired
    CafeRepository cafeRepository;
    @Autowired
    CafeBoardRepository cafeBoardRepository;
    @Autowired
    CafeLevelRepository cafeLevelRepository;
    @Autowired
    NewsCategoryRepository newsCategoryRepository;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    BlogBoardRepository blogBoardRepository;

    @Value("${spring.datasource.password}")
    private String password;

//    @BeforeAll
//    void setupData() {
//        makeDefaultData();
//    }

    private static final String[] topics = {
            "인공지능", "클라우드", "개발자", "오픈소스", "스타트업", "프로그래밍 언어", "AI 윤리", "사물인터넷", "웹 개발", "빅데이터"
    };

    private static final String[] sentenceTemplates = {
            "%s 기술이 급부상하고 있습니다. 전문가들은 이 기술이 향후 산업에 큰 영향을 미칠 것으로 전망합니다.",
            "최근 %s 관련 사건이 업계를 뜨겁게 달구고 있습니다. 특히 개발자 커뮤니티에서 활발한 논의가 이어지고 있습니다.",
            "%s 분야에서의 혁신이 계속되고 있습니다. 주요 기업들은 이를 기반으로 새로운 서비스를 출시하고 있습니다.",
            "%s 트렌드는 다양한 산업군에서 채택되고 있습니다. 사용성 향상과 비용 절감이 주요 장점으로 꼽힙니다.",
            "많은 사람들이 %s에 관심을 갖고 학습을 시작하고 있습니다. 관련 교육 콘텐츠의 수요도 빠르게 증가하고 있습니다."
    };

    @Test
    void makeDefaultData(){ //기본 데이터가 하나도 없을 경우 꼭 먼저 실행해주세요.
        /* 카페 관련 기본 정보 */
        RegisterRequest request = new RegisterRequest("test123@naver.com", "testID", "test123", "tester", "010-1239-0000", "testNickname");
        Member member = Member.of(request, "test123");
        memberRepository.save(member);

        Cafe cafe = Cafe.of("testCafe", "testCafe Description");
        cafeRepository.save(cafe);

        CafeBoard cafeBoard = CafeBoard.of(cafe, "CafeBoard1");
        cafeBoardRepository.save(cafeBoard);

        CafeLevel level = CafeLevel.of(cafe, "cafeADMIN", "gradeDescription", false, 5, 0, 0, 0);
        cafeLevelRepository.save(level);

        CafeMember cafeMember = CafeMember.of(cafe, member, level, "nickname1");
        cafeMemberRepository.save(cafeMember);

        /* 뉴스 관련 기본 정보 */
        NewsCategory parentCategory = NewsCategory.of("testCategory", null);
        newsCategoryRepository.save(parentCategory);

        NewsCategory childCategory = NewsCategory.of("childCategory", parentCategory);
        newsCategoryRepository.save(childCategory);

        /* 블로그 관련 기본 정보 */
        Blog blog = Blog.of(member, "testBlog", "testDescription");
        blogRepository.save(blog);

        BlogBoard blogBoard = BlogBoard.of(blog, "testCategory");
        blogBoardRepository.save(blogBoard);
    }


    @Test
    void makeCafePostData() throws SQLException { //카페 게시글 생성
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/portalSite",
                "root",
                password //이곳에 password를 입력해주세요. ex) "1234"
        );
       conn.setAutoCommit(false);

        Faker faker = new Faker(new Locale("ko"));

        String sql = "INSERT INTO cafe_post (cafe_id, cafe_board_id, cafe_member_id, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        int batchSize = 1000;
        int cnt = 0;
        // 카페 게시글 50만개 생성
        for(int i=0; i<500000; i++){
            String topic = faker.options().option(topics);
            pstmt.setLong(1, 1L);
            pstmt.setLong(2, 1L);
            pstmt.setLong(3, 1L);
            pstmt.setString(4, String.format("[%s] %s",topic, faker.book().title()));
            pstmt.setString(5, String.format(faker.options().option(sentenceTemplates),topic));
            pstmt.addBatch();
            cnt++;

            if(cnt%batchSize==0){
                pstmt.executeBatch();
                conn.commit();
            }
        }

        pstmt.executeBatch();
        conn.commit();
        pstmt.close();
        conn.close();

        System.out.println("✅ 데이터 삽입 완료!");
    }

    @Test
    void makeNewsData() throws SQLException { //뉴스 기사 생성
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/portalSite",
                "root",
                password //이곳에 password를 입력해주세요. ex) "1234"
        );
        conn.setAutoCommit(false);

        Faker faker = new Faker(new Locale("ko"));

        String sql = "INSERT INTO news (member_id, news_category_id, news_title, description, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        int batchSize = 1000;
        int cnt = 0;

        // 뉴스 기사 50만개 생성
        for(int i=0; i<500000; i++){
            String topic = faker.options().option(topics);
            pstmt.setLong(1, 1L);
            pstmt.setLong(2, 2L);
            pstmt.setString(3, String.format("[%s] %s",topic, faker.book().title()));
            pstmt.setString(4,  String.format(faker.options().option(sentenceTemplates),topic));
            pstmt.addBatch();
            cnt++;

            if(cnt%batchSize==0){
                pstmt.executeBatch();
                conn.commit();
            }
        }

        pstmt.executeBatch();
        conn.commit();
        pstmt.close();
        conn.close();

        System.out.println("✅ 데이터 삽입 완료!");
    }

    @Test
    void makeBlogPostData() throws SQLException { //블로그 게시글 생성
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/portalSite",
                "root",
                password //이곳에 password를 입력해주세요. ex) "1234"
        );
        conn.setAutoCommit(false);

        Faker faker = new Faker(new Locale("ko"));

        String sql = "INSERT INTO blog_post (member_id, blog_board_id, blog_id, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        int batchSize = 1000;
        int cnt = 0;

        // 블로그 게시글 50만개 생성
        for(int i=0; i<500000; i++){
            String topic = faker.options().option(topics);
            pstmt.setLong(1, 1L);
            pstmt.setLong(2, 1L);
            pstmt.setLong(3, 1L);
            pstmt.setString(4, String.format("[%s] %s",topic, faker.book().title()));
            pstmt.setString(5,  String.format(faker.options().option(sentenceTemplates),topic));
            pstmt.addBatch();
            cnt++;

            if(cnt%batchSize==0){
                pstmt.executeBatch();
                conn.commit();
            }
        }

        pstmt.executeBatch();
        conn.commit();
        pstmt.close();
        conn.close();

        System.out.println("✅ 데이터 삽입 완료!");
    }
}

package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getBalanceFirstCardAfterTransfer;
import static ru.netology.data.DataHelper.getBalanceSecondCardAfterTransfer;

public class MoneyTransferTest {

    @BeforeEach
    public void setUp() {
        val loginPage = open("http://localhost:9999/", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }


    @Test
    public void shouldTransferFromOneToSecondCard() {
        val dashboardPage = new DashboardPage();
        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getFirstCardInfo();
        String amount = "150";
        transferPage.transferCard(infoCard, amount);
        assertEquals(balanceFirstCard - Integer.parseInt(amount), dashboardPage.getFirstCardBalance());
        assertEquals(balanceSecondCard + Integer.parseInt(amount), dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldTransferFromSecondToOne() {
        val dashboardPage = new DashboardPage();
        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardInfo();
        String amount = "300";
        transferPage.transferCard(infoCard, amount);
        assertEquals(balanceFirstCard + Integer.parseInt(amount), dashboardPage.getFirstCardBalance());
        assertEquals(balanceSecondCard - Integer.parseInt(amount), dashboardPage.getSecondCardBalance());
    }
    @Test
    public void shouldTransferAboveCardBalance() {
        val dashboardPage = new DashboardPage();
        val transferPage = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getFirstCardInfo();
        String amount = "25000";
        transferPage.transferCard(infoCard, amount);
        transferPage.getError();
    }


}
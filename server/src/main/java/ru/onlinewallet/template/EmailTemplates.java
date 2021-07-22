package ru.onlinewallet.template;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailTemplates {

    public final String restoreAccessMailTemplate = """
            <div width="100%" bgcolor="#fff" style="margin:0">
              <center style="width:100%;background:#fff">
                <div style="max-width:600px;margin:auto">
                  <table role="presentation" cellspacing="0" cellpadding="0" border="0" align="center" width="100%"
                         style="max-width:600px">
                    <tbody>
                    <tr>
                      <td bgcolor="#ffffff" style="text-align:center">
                        <img src="http://online-wallet.ru/logo.7c3a151eeabed7d2e877.png" width="202" height="" alt="alt_text"
                             border="0" align="center" style="max-width:202px;width:100%;height:auto" class="CToWUd">
                      </td>
                    </tr>

                    <tr>
                      <td bgcolor="#ffffff">
                        <table role="presentation" cellspacing="0" cellpadding="0" border="0" width="100%">
                          <tbody>
                          <tr>
                            <td
                              style="padding:40px;font-family:sans-serif;font-size:15px;line-height:20px;color:#555555!important;padding-top:10px">

                              <br><br>

                              Уважаемый <b>USERNAME</b>, Вы сделали запрос на восстановление доступа к
                              аккаунту, нажмите на ссылки ниже, чтобы восстановить доступ к своему аккаунту.

                              <br><br>

                              IP адресс отправителя: <b>SENDER_IP_ADDRESS</b>

                              <br><br>

                              <table role="presentation" cellspacing="0" cellpadding="0" border="0" align="center"
                                     style="margin:0">
                                <tbody>
                                <tr>
                                  <td>
                                    <span>
                                       <span>Для сброса пароля на сайте пройдите по ссылке:</span><br><a
                                      href="RESET_PASSWORD_URL" target="_blank">RESET_PASSWORD_URL</a>
                                      <br><br><span>Для сброса двухфакторной аутентификации перейдите по ссылке:</span><br><a
                                      href="RESET_TWO_FACTOR_AUTH" target="_blank">RESET_TWO_FACTOR_AUTH</a>
                                    </span>
                                  </td>
                                </tr>
                                </tbody>
                              </table>
                            </td>
                          </tr>
                          </tbody>
                        </table>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </div>
              </center>
              <div class="yj6qo"></div>
              <div class="adL">
              </div>
            </div>
            """;

    public final String restorePasswordTemplate = """
            <div width="100%" bgcolor="#fff" style="margin:0">
              <center style="width:100%;background:#fff">
                <div style="max-width:600px;margin:auto">
                  <table role="presentation" cellspacing="0" cellpadding="0" border="0" align="center" width="100%"
                         style="max-width:600px">
                    <tbody>
                    <tr>
                      <td bgcolor="#ffffff" style="text-align:center">
                        <img src="http://online-wallet.ru/logo.7c3a151eeabed7d2e877.png" width="202" height="" alt="alt_text"
                             border="0" align="center" style="max-width:202px;width:100%;height:auto" class="CToWUd">
                      </td>
                    </tr>
                        
                    <tr>
                      <td bgcolor="#ffffff">
                        <table role="presentation" cellspacing="0" cellpadding="0" border="0" width="100%">
                          <tbody>
                          <tr>
                            <td
                              style="padding:40px;font-family:sans-serif;font-size:15px;line-height:20px;color:#555555!important;padding-top:10px">
                        
                              <br><br>
                        
                              Уважаемый <b>USERNAME</b>,Согласно Вашему запросу для Вас был сгенерирован новый пароль. Для входа на
                              сайт используйте следующие данные:
                        
                        
                              <br><br>
                        
                              <table role="presentation" cellspacing="0" cellpadding="0" border="0" align="center"
                                     style="margin:0">
                                <tbody>
                                <tr>
                                  <td>
                                                <span>
                                                   <span>Логин: USERNAME</span><br>
                                                  <span>Пароль: PASSWORD_REPLACE</span>
                                                </span><br><br>
                                  </td>
                                </tr>
                                </tbody>
                              </table>
                        
                              После авторизации на сайте Вы сможете изменить данный пароль на любой другой.<br><br>
                        
                              С уважением,
                              администрация сайта <a href="http://online-wallet.ru/">http://online-wallet.ru/</a>
                            </td>
                          </tr>
                          </tbody>
                        </table>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </div>
              </center>
              <div class="yj6qo"></div>
              <div class="adL">
              </div>
            </div>
            """;
}

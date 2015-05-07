package ru.alexeymz.web.controllers;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import ru.alexeymz.web.core.utils.StreamUtils;
import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

import static ru.alexeymz.web.core.utils.EscapeUtils.wrapNull;

@WebServlet("/profile")
@MultipartConfig
public class ProfileController extends BaseAppController {
    public static final long MAX_AVATAR_FILE_SIZE = 100 * 1024;

    private String noAvatarPicture = "";
    private UserRepository userRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.userRepository = (UserRepository)context.getAttribute(
                UserRepository.ATTRIBUTE);
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("no_avatar.jpg")) {
            noAvatarPicture = Base64.encode(StreamUtils.readToEnd(is));
        } catch (IOException ex) {
            log("Could not load NO_AVATAR picture", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getRemoteUser();
        if (username == null) { resp.sendError(403); return; }
        User user = userRepository.findByUsername(username);
        if (user == null) { resp.sendError(400); return; }

        renderView(req, resp, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getRemoteUser();
        if (username == null) { resp.sendError(403); return; }
        User user = userRepository.findByUsername(username);
        if (user == null) { resp.sendError(400); return; }

        String firstName = req.getParameter("firstName");
        if (firstName != null && firstName.length() < 100) {
            user.setFirstName(firstName);
        }
        String defaultTab = req.getParameter("defaultItemTab");
        if (defaultTab != null && defaultTab.length() < 10) {
            user.setDefaultTab(defaultTab.isEmpty() ? null : defaultTab);
        }
        Part avatarPicture = req.getPart("avatarPicture");
        if (avatarPicture != null) {
            try {
                String avatarSrc = Base64.encode(StreamUtils.readToEnd(avatarPicture.getInputStream()));
                if (avatarSrc != null && avatarSrc.length() > 0 &&
                    avatarSrc.length() <= MAX_AVATAR_FILE_SIZE)
                {
                    user.setAvatar(avatarSrc);
                }
            } catch (IOException ex) {
                log("Avatar transformation to base64 failed", ex);
            }
        }

        renderView(req, resp, user);
    }

    private void renderView(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.setAttribute("maxAvatarSizeInKiB", MAX_AVATAR_FILE_SIZE / 1024);

        req.setAttribute("firstName", wrapNull(user.getFirstName()));
        req.setAttribute("defaultItemTab", wrapNull(user.getDefaultTab()));
        req.setAttribute("avatarSrc", user.getAvatar() == null
                ? noAvatarPicture : user.getAvatar());

        req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
    }
}

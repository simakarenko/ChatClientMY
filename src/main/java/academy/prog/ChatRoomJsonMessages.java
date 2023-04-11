package academy.prog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatRoomJsonMessages {
    private List<Message> list = new ArrayList<>();

    public ChatRoomJsonMessages(List<Message> sourceList, int fromIndex) {
        for (int i = fromIndex; i < sourceList.size(); i++)
            list.add(sourceList.get(i));
    }

    public List<Message> getList() {
        return Collections.unmodifiableList(list);
    }
}

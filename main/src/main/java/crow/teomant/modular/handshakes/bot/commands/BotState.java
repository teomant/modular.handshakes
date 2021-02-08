package crow.teomant.modular.handshakes.bot.commands;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import crow.teomant.modular.handshakes.user.domain.model.User;
import java.util.HashMap;
import java.util.Map;
import org.telegram.telegrambots.meta.api.objects.Message;

public class BotState {

    private final Map<Integer, Map<Integer, User>> registrationOptions = new HashMap<>();
    private final Map<Integer, Map<Integer, User>> addOptions = new HashMap<>();
    private final Map<Integer, Map<Integer, User>> findOptions = new HashMap<>();
    private final RelationCommandState relationCommandState = new RelationCommandState();

    public boolean isRegistrationInProcess(Message message) {
        return registrationOptions.containsKey(message.getFrom().getId());
    }

    public void addRegistrationOptions(Message message, Map<Integer, User> options) {
        registrationOptions.put(message.getFrom().getId(), options);
    }

    public Map<Integer, User> getRegistrationOptions(Message message) {
        return registrationOptions.get(message.getFrom().getId());
    }

    public void removeRegistrationOptions(Message message) {
        registrationOptions.remove(message.getFrom().getId());
    }

    public boolean isFindInProcess(Message message) {
        return findOptions.containsKey(message.getFrom().getId());
    }

    public void addFindOptions(Message message, Map<Integer, User> options) {
        findOptions.put(message.getFrom().getId(), options);
    }

    public Map<Integer, User> getFindOptions(Message message) {
        return findOptions.get(message.getFrom().getId());
    }

    public void removeFindOptions(Message message) {
        findOptions.remove(message.getFrom().getId());
    }

    public boolean isAddInProcess(Message message) {
        return addOptions.containsKey(message.getFrom().getId());
    }

    public void addAddOptions(Message message, Map<Integer, User> options) {
        addOptions.put(message.getFrom().getId(), options);
    }

    public Map<Integer, User> getAddOptions(Message message) {
        return addOptions.get(message.getFrom().getId());
    }

    public void removeAddOptions(Message message) {
        addOptions.remove(message.getFrom().getId());
    }

    public RelationCommandState getRelationCommandState() {
        return relationCommandState;
    }

    public void cancel(Message message) {
        registrationOptions.remove(message.getFrom().getId());
        addOptions.remove(message.getFrom().getId());
        findOptions.remove(message.getFrom().getId());
        relationCommandState.removeRelationsOptions(message);
    }

    public static class RelationCommandState {

        private final Map<Integer, Map<Integer, User>> fromOptions = new HashMap<>();
        private final Map<Integer, Map<Integer, User>> toOptions = new HashMap<>();
        private final Map<Integer, RelationType> relationTypeMap = new HashMap<>();
        private final Map<Integer, User> fromMap = new HashMap<>();
        private final Map<Integer, User> toMap = new HashMap<>();

        public Map<Integer, User> getFromOptions(Message message) {
            return fromOptions.get(message.getFrom().getId());
        }

        public Map<Integer, User> getToOptions(Message message) {
            return toOptions.get(message.getFrom().getId());
        }

        public User getFrom(Message message) {
            return fromMap.get(message.getFrom().getId());
        }

        public User getTo(Message message) {
            return toMap.get(message.getFrom().getId());
        }

        public RelationType getRelation(Message message) {
            return relationTypeMap.get(message.getFrom().getId());
        }

        public boolean isChoosingFrom(Message message) {
            return fromOptions.containsKey(message.getFrom().getId());
        }

        public boolean isChoosingTo(Message message) {
            return toOptions.containsKey(message.getFrom().getId());
        }

        public void addRelation(Message message, RelationType relationType) {
            relationTypeMap.put(message.getFrom().getId(), relationType);
        }

        public void addFromOptions(Message message, Map<Integer, User> options) {
            fromOptions.put(message.getFrom().getId(), options);
        }

        public void addToOptions(Message message, Map<Integer, User> options) {
            toOptions.put(message.getFrom().getId(), options);
        }

        public void addFrom(Message message, User from) {
            fromOptions.remove(message.getFrom().getId());
            fromMap.put(message.getFrom().getId(), from);
        }

        public void addTo(Message message, User to) {
            toOptions.remove(message.getFrom().getId());
            toMap.put(message.getFrom().getId(), to);
        }

        public void removeRelationsOptions(Message message) {
            fromOptions.remove(message.getFrom().getId());
            toOptions.remove(message.getFrom().getId());
            relationTypeMap.remove(message.getFrom().getId());
            fromMap.remove(message.getFrom().getId());
            toMap.remove(message.getFrom().getId());
        }


    }
}

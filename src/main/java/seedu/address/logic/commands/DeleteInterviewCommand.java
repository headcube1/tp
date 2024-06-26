package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.interview.Interview;


/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteInterviewCommand extends Command {

    public static final String COMMAND_WORD = "delete_interview";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the Interview identified by the index in the displayed interview list.\n"
            + "Parameters: Index\n"
            + "Example: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_DELETE_INTERVIEW_SUCCESS = "Interview Deleted: %1$s";

    private final Integer targetInt;


    public DeleteInterviewCommand(int targetInt) {
        this.targetInt = targetInt;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Interview> lastShownList = model.getFilteredInterviewList();
        Interview interview;
        try {
            interview = lastShownList.get(targetInt);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(Messages.MESSAGE_INTERVIEW_NOT_IN_LIST);
        }

        interview.getApplicant().revertCurrentStatus(model);
        interview.getInterviewer().updateCurrentStatusToReflectDeletedInterview(model, interview.getApplicant());
        model.deleteInterview(interview);
        return new CommandResult(String.format(MESSAGE_DELETE_INTERVIEW_SUCCESS, Messages.formatInterview(interview)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteInterviewCommand)) {
            return false;
        }

        DeleteInterviewCommand otherDeleteInterviewCommand = (DeleteInterviewCommand) other;
        return targetInt.equals(otherDeleteInterviewCommand.targetInt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetInt", targetInt)
                .toString();
    }
}

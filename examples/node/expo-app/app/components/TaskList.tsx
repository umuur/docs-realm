import { View, FlatList, StyleSheet } from 'react-native';
import { Realm } from '@realm/react';
import TaskItem from './TaskItem';
import TaskContext, { Task } from '../models/Task';
const { RealmProvider } = TaskContext;
const { useRealm, useQuery, useObject } = TaskContext;

// :code-block-start: tasklist-use-query-example
function TaskList({onToggleTaskStatus, onDeleteTask}) {
  const tasks = useQuery(Task);
  return (
    <View style={styles.listContainer}>
      <FlatList
        data={tasks}
        keyExtractor={task => task._id.toString()}
        renderItem={({item}) => (
          <TaskItem
            description={item.description}
            isComplete={item.isComplete}
            onToggleStatus={() => onToggleTaskStatus(item)}
            onDelete={() => onDeleteTask(item)}
          />
        )}
      />
    </View>
  );
}
// :code-block-end:

const styles = StyleSheet.create({
  listContainer: {
    flex: 1,
    justifyContent: 'center',
  },
});

export default TaskList;

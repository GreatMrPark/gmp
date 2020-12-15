class MyCalendar extends React.Component {
  getIssues(year) {
    // Load data from GitHub API
    return fetch(`https://api.github.com/search/issues?q=repo:Paul-DS/bootstrap-year-calendar%20created:${year}-01-01..${year}-12-31`)
      .then(result => result.json())
      .then(result => {
        if (result.items) {
          return result.items.map(r => ({
            startDate: new Date(r.created_at),
            endDate: new Date(r.created_at),
            name: '#' + r.number + ' - ' + r.title,
            details: r.comments + ' comments'
          }));
        }
        
        return [];
      });
  }
  
  handleDayEnter(e) {
    if (e.events.length > 0) {
      var content = '';
                
      for (var i in e.events) {
        content += '<div class="event-tooltip-content">'
          + '<div class="event-name" style="color:' + e.events[i].color + '">' + e.events[i].name + '</div>'
          + '<div class="event-details">' + e.events[i].details + '</div>'
          + '</div>';
      }
      
      if (this.tooltip != null) {
        this.tooltip.destroy();
        this.tooltip = null;
      }
      
      this.tooltip = tippy(e.element, {
          placement: 'right',
          content: content,
          animateFill: false,
          animation: 'shift-away',
          arrow: true
      });
      this.tooltip.show();
    }
  }
    
  handleDayLeave() {
    if (this.tooltip !== null) {
      this.tooltip.destroy();
      this.tooltip = null;
    }
  }

  render() {
    return (
      <div>
        <Calendar
          dataSource={year => this.getIssues(year)}
          onDayEnter={e => this.handleDayEnter(e)}
          onDayLeave={() => this.handleDayLeave()}
          />
      </div>
    );
  }
}

ReactDOM.render(<MyCalendar />, document.getElementById('app'));